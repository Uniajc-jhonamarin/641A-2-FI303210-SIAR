package com.siar.servicio;

import com.siar.modelo.Rol;
import com.siar.modelo.Usuario;
import com.siar.repositorio.RolRepository;
import com.siar.repositorio.UsuarioRepository;
import com.siar.seguridad.SesionActual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    private UsuarioService usuarioService;
    private List<Usuario> bdUsuarios;
    private Rol rolAdmin;

    @BeforeEach
    void setUp() {
        SesionActual.getInstancia().cerrarSesion();
        bdUsuarios = new ArrayList<>();
        rolAdmin = new Rol(1, "Administrador", "Acceso total");

        UsuarioRepository usuarioRepoFake = new UsuarioRepository() {
            @Override
            public Usuario obtenerPorEmail(String email) {
                return bdUsuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
            }

            @Override
            public List<Usuario> obtenerTodos() {
                return bdUsuarios;
            }

            @Override
            public boolean guardar(Usuario u) {
                u.setId(bdUsuarios.size() + 1);
                return bdUsuarios.add(u);
            }

            @Override
            public boolean actualizar(Usuario u, boolean actualizarPassword) {
                return bdUsuarios.stream().anyMatch(x -> x.getId() == u.getId());
            }

            @Override
            public boolean eliminar(int id) {
                return bdUsuarios.removeIf(x -> x.getId() == id);
            }

            @Override
            public boolean actualizarPasswordPorEmail(String email, String nuevoPasswordHash) {
                return true;
            }
        };

        RolRepository rolRepoFake = () -> List.of(
                rolAdmin,
                new Rol(2, "Mesero", "Toma pedidos")
        );

        usuarioService = new UsuarioService(usuarioRepoFake, rolRepoFake);
    }

    @Test
    void debeListarRolesYRegistrarUsuarioValidoConBCrypt() {
        assertEquals(2, usuarioService.obtenerRoles().size());

        Usuario u = new Usuario();
        u.setNombre("Carlos Perez");
        u.setEmail("carlos@siar.com");
        u.setRol(rolAdmin);
        u.setActivo(true);

        assertTrue(usuarioService.registrarUsuario(u, "clave123"));
        assertEquals(1, usuarioService.obtenerTodosLosUsuarios().size());
        assertTrue(BCrypt.checkpw("clave123", u.getPasswordHash()));
    }

    @Test
    void debeRechazarRegistroConDatosInvalidos() {
        Usuario u = new Usuario();
        assertFalse(usuarioService.registrarUsuario(u, "1234")); // sin nombre

        u.setNombre("Ana");
        assertFalse(usuarioService.registrarUsuario(u, "1234")); // sin email

        u.setEmail("ana@siar.com");
        assertFalse(usuarioService.registrarUsuario(u, "12"));   // password corta

        assertFalse(usuarioService.registrarUsuario(u, "1234")); // sin rol
    }

    @Test
    void debeActualizarUsuarioConYSinNuevaPassword() {
        Usuario u = new Usuario();
        u.setNombre("Laura");
        u.setEmail("laura@siar.com");
        u.setRol(rolAdmin);
        usuarioService.registrarUsuario(u, "pass1");

        // Actualizar sin cambiar password
        assertTrue(usuarioService.actualizarUsuario(u, ""));
        // Actualizar cambiando password
        assertTrue(usuarioService.actualizarUsuario(u, "nuevaPass"));
        assertTrue(BCrypt.checkpw("nuevaPass", u.getPasswordHash()));
        // Password nueva demasiado corta
        assertFalse(usuarioService.actualizarUsuario(u, "12"));
        // ID inválido
        u.setId(0);
        assertFalse(usuarioService.actualizarUsuario(u, ""));
    }

    @Test
    void debeEliminarUsuarioPeroBloquearAutoEliminacion() {
        Usuario u = new Usuario();
        u.setNombre("Admin");
        u.setEmail("admin@siar.com");
        u.setRol(rolAdmin);
        usuarioService.registrarUsuario(u, "admin123");

        // Simular sesión activa con este mismo usuario
        SesionActual.getInstancia().setUsuarioActual(u);
        assertFalse(usuarioService.eliminarUsuario(u.getId()));
        assertFalse(usuarioService.eliminarUsuario(0));

        // Cerrar sesión y eliminar
        SesionActual.getInstancia().cerrarSesion();
        assertTrue(usuarioService.eliminarUsuario(u.getId()));
    }
}
