package com.siar.persistencia;

import com.siar.configuracion.ConexionBaseDatos;
import com.siar.modelo.Rol;
import com.siar.modelo.Usuario;
import com.siar.repositorio.UsuarioRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    @Override
    public Usuario obtenerPorEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT u.id, u.nombre, u.email, u.password_hash, u.activo, "
                + "r.id AS rol_id, r.nombre AS rol_nombre, r.descripcion AS rol_desc "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.rol_id = r.id "
                + "WHERE u.email = ? AND u.activo = 1";

        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar el usuario por email: " + e.getMessage());
        }
        return usuario;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.id, u.nombre, u.email, u.password_hash, u.activo, "
                + "r.id AS rol_id, r.nombre AS rol_nombre, r.descripcion AS rol_desc "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.rol_id = r.id "
                + "ORDER BY u.id ASC";

        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public boolean guardar(Usuario u) {
        String sql = "INSERT INTO usuarios (rol_id, nombre, email, password_hash, activo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, u.getRol().getId());
            stmt.setString(2, u.getNombre());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getPasswordHash());
            stmt.setBoolean(5, u.isActivo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario u, boolean actualizarPassword) {
        String sql = actualizarPassword
                ? "UPDATE usuarios SET rol_id=?, nombre=?, email=?, password_hash=?, activo=? WHERE id=?"
                : "UPDATE usuarios SET rol_id=?, nombre=?, email=?, activo=? WHERE id=?";

        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, u.getRol().getId());
            stmt.setString(2, u.getNombre());
            stmt.setString(3, u.getEmail());

            if (actualizarPassword) {
                stmt.setString(4, u.getPasswordHash());
                stmt.setBoolean(5, u.isActivo());
                stmt.setInt(6, u.getId());
            } else {
                stmt.setBoolean(4, u.isActivo());
                stmt.setInt(5, u.getId());
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPasswordHash(rs.getString("password_hash"));
        usuario.setActivo(rs.getBoolean("activo"));

        Rol rol = new Rol(
                rs.getInt("rol_id"),
                rs.getString("rol_nombre"),
                rs.getString("rol_desc")
        );
        usuario.setRol(rol);
        return usuario;
    }

    @Override
    public boolean actualizarPasswordPorEmail(String email, String nuevoPasswordHash) {
        String sql = "UPDATE usuarios SET password_hash = ? WHERE email = ? AND activo = 1";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoPasswordHash);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al recuperar contraseña: " + e.getMessage());
            return false;
        }
    }
}
