package com.siar.servicio;

import com.siar.modelo.Usuario;
import com.siar.repositorio.UsuarioRepository;
import com.siar.seguridad.SesionActual;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias por constructor
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean iniciarSesion(String email, String passwordPlano) {
        Usuario usuario = usuarioRepository.obtenerPorEmail(email);

        if (usuario == null) {
            // El usuario no existe o está inactivo
            return false;
        }

        // Se compara la contraseña escrita por el usuario contra el Hash de la BD
        if (BCrypt.checkpw(passwordPlano, usuario.getPasswordHash())) {
            // Si coincide, registramos al usuario en la sesión global (Singleton)
            SesionActual.getInstancia().setUsuarioActual(usuario);
            return true;
        }

        return false; // Contraseña incorrecta
    }

    public void cerrarSesion() {
        SesionActual.getInstancia().cerrarSesion();
    }

    public boolean recuperarPassword(String email, String nuevaPasswordPlano) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        if (nuevaPasswordPlano == null || nuevaPasswordPlano.trim().length() < 4) {
            return false;
        }

        Usuario u = usuarioRepository.obtenerPorEmail(email.trim());
        if (u == null) {
            return false;
        }

        String nuevoHash = BCrypt.hashpw(nuevaPasswordPlano, BCrypt.gensalt());
        boolean ok = usuarioRepository.actualizarPasswordPorEmail(email.trim(), nuevoHash);
        if (ok) {
            new AuditoriaService().registrarEvento("RECUPERAR_PASSWORD", "Restablecimiento para: " + email);
        }
        return ok;
    }
}
