package com.siar.repositorio;

import com.siar.modelo.Usuario;
import java.util.List;

public interface UsuarioRepository {
    Usuario obtenerPorEmail(String email);
    List<Usuario> obtenerTodos();
    boolean guardar(Usuario usuario);
    boolean actualizar(Usuario usuario, boolean actualizarPassword);
    boolean eliminar(int id);
    boolean actualizarPasswordPorEmail(String email, String nuevoPasswordHash);
}