package com.siar.persistencia;

import com.siar.configuracion.ConexionBaseDatos;
import com.siar.modelo.Rol;
import com.siar.modelo.Usuario;
import com.siar.repositorio.UsuarioRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    @Override
    public Usuario obtenerPorEmail(String email) {
        Usuario usuario = null;
        // Consulta uniendo usuarios y roles para traer toda la información de una vez
        String sql = "SELECT u.id, u.nombre, u.email, u.password_hash, u.activo, " +
                     "r.id AS rol_id, r.nombre AS rol_nombre, r.descripcion AS rol_desc " +
                     "FROM usuarios u " +
                     "INNER JOIN roles r ON u.rol_id = r.id " +
                     "WHERE u.email = ? AND u.activo = 1";

        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPasswordHash(rs.getString("password_hash"));
                    usuario.setActivo(rs.getBoolean("activo"));
                    
                    Rol rol = new Rol();
                    rol.setId(rs.getInt("rol_id"));
                    rol.setNombre(rs.getString("rol_nombre"));
                    rol.setDescripcion(rs.getString("rol_desc"));
                    
                    usuario.setRol(rol);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar el usuario por email: " + e.getMessage());
        }
        
        return usuario;
    }
}