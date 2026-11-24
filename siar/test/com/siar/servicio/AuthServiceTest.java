package com.siar.servicio;

import com.siar.modelo.Rol;
import com.siar.modelo.Usuario;
import com.siar.repositorio.UsuarioRepository;
import com.siar.seguridad.SesionActual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        SesionActual.getInstancia().cerrarSesion();

        Usuario admin = new Usuario();
        admin.setId(1);
        admin.setNombre("Admin Sistema");
        admin.setEmail("admin@siar.com");
        admin.setPasswordHash(BCrypt.hashpw("admin123", BCrypt.gensalt()));
        admin.setActivo(true);
        admin.setRol(new Rol(1, "Administrador", "Acceso total"));

        // Repositorio falso en memoria
        UsuarioRepository repoFake = new UsuarioRepository() {
            @Override
            public Usuario obtenerPorEmail(String email) {
                return "admin@siar.com".equals(email) ? admin : null;
            }

            @Override
            public List<Usuario> obtenerTodos() {
                return List.of(admin);
            }

            @Override
            public boolean guardar(Usuario u) {
                return true;
            }

            @Override
            public boolean actualizar(Usuario u, boolean b) {
                return true;
            }

            @Override
            public boolean eliminar(int id) {
                return true;
            }

            @Override
            public boolean actualizarPasswordPorEmail(String email, String nuevoPasswordHash) {
                return true;
            }
        };

        authService = new AuthService(repoFake);
    }

    @Test
    void loginExitosoDebeGuardarUsuarioEnSesionActual() {
        boolean resultado = authService.iniciarSesion("admin@siar.com", "admin123");

        assertTrue(resultado);
        assertNotNull(SesionActual.getInstancia().getUsuarioActual());
        assertEquals("Admin Sistema", SesionActual.getInstancia().getUsuarioActual().getNombre());
    }

    @Test
    void loginConPasswordIncorrectaDebeFallar() {
        boolean resultado = authService.iniciarSesion("admin@siar.com", "claveMala");

        assertFalse(resultado);
        assertNull(SesionActual.getInstancia().getUsuarioActual());
    }

    @Test
    void loginConUsuarioInexistenteDebeFallar() {
        assertFalse(authService.iniciarSesion("noexiste@siar.com", "admin123"));
    }

    @Test
    void cerrarSesionDebeLimpiarSingleton() {
        authService.iniciarSesion("admin@siar.com", "admin123");
        authService.cerrarSesion();
        assertNull(SesionActual.getInstancia().getUsuarioActual());
    }

}
