package com.siar.servicio;

import com.siar.modelo.Rol;
import com.siar.modelo.Usuario;
import com.siar.repositorio.RolRepository;
import com.siar.repositorio.UsuarioRepository;
import com.siar.seguridad.SesionActual;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;

    public UsuarioService(UsuarioRepository usuarioRepo, RolRepository rolRepo) {
        this.usuarioRepo = usuarioRepo;
        this.rolRepo = rolRepo;
    }

    public List<Rol> obtenerRoles() {
        return rolRepo.obtenerTodos();
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepo.obtenerTodos();
    }

    public boolean registrarUsuario(Usuario u, String passwordPlano) {
        if (u.getNombre() == null || u.getNombre().trim().isEmpty()) {
            return false;
        }
        if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
            return false;
        }
        if (passwordPlano == null || passwordPlano.trim().length() < 4) {
            return false;
        }
        if (u.getRol() == null) {
            return false;
        }

        // Encriptar contraseña con BCrypt antes de guardar
        String hash = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
        u.setPasswordHash(hash);

        boolean ok = usuarioRepo.guardar(u);
        if (ok) {
            new AuditoriaService().registrarEvento("CREAR_USUARIO", "Usuario registrado: " + u.getEmail());
        }
        return ok;
    }

    public boolean actualizarUsuario(Usuario u, String nuevaPasswordPlano) {
        if (u.getId() <= 0) {
            return false;
        }
        if (u.getNombre() == null || u.getNombre().trim().isEmpty()) {
            return false;
        }
        if (u.getEmail() == null || u.getEmail().trim().isEmpty()) {
            return false;
        }
        if (u.getRol() == null) {
            return false;
        }

        boolean cambiarPass = nuevaPasswordPlano != null && !nuevaPasswordPlano.trim().isEmpty();
        if (cambiarPass) {
            if (nuevaPasswordPlano.trim().length() < 4) {
                return false;
            }
            u.setPasswordHash(BCrypt.hashpw(nuevaPasswordPlano, BCrypt.gensalt()));
        }

        boolean ok = usuarioRepo.actualizar(u, cambiarPass);
        if (ok) {
            new AuditoriaService().registrarEvento("ACTUALIZAR_USUARIO", "Usuario actualizado ID: " + u.getId() + " (" + u.getEmail() + ")");
        }
        return ok;
    }

    public boolean eliminarUsuario(int id) {
        if (id <= 0) {
            return false;
        }

        // Regla de seguridad: no permitir que el usuario logueado se elimine a sí mismo
        Usuario actual = SesionActual.getInstancia().getUsuarioActual();
        if (actual != null && actual.getId() == id) {
            return false;
        }

        boolean ok = usuarioRepo.eliminar(id);
        if (ok) {
            new AuditoriaService().registrarEvento("ELIMINAR_USUARIO", "Usuario eliminado ID: " + id);
        }
        return ok;
    }
}
