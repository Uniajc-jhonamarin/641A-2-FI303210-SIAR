package com.siar.persistencia;

import com.siar.configuracion.ConexionBaseDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditoriaRepositoryImpl {

    public void registrar(String usuarioEmail, String accion, String detalle) {
        String sql = "INSERT INTO auditoria (usuario_email, accion, detalle) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuarioEmail);
            stmt.setString(2, accion);
            stmt.setString(3, detalle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
        }
    }
}