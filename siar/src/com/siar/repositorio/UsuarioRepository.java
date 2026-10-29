package com.siar.repositorio;

import com.siar.modelo.Usuario;

public interface UsuarioRepository {
    Usuario obtenerPorEmail(String email);
}