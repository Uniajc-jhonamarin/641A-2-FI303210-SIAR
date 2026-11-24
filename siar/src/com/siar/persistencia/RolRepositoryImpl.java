package com.siar.persistencia;

import com.siar.configuracion.ConexionBaseDatos;
import com.siar.modelo.Rol;
import com.siar.repositorio.RolRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolRepositoryImpl implements RolRepository {

    @Override
    public List<Rol> obtenerTodos() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM roles ORDER BY id ASC";

        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roles.add(new Rol(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener roles: " + e.getMessage());
        }
        return roles;
    }
}