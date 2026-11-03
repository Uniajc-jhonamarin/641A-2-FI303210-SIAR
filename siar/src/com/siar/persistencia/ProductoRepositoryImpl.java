package com.siar.persistencia;

import com.siar.configuracion.ConexionBaseDatos;
import com.siar.modelo.Categoria;
import com.siar.modelo.Producto;
import com.siar.repositorio.ProductoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryImpl implements ProductoRepository {

    @Override
    public boolean guardar(Producto p) {
        String sql = "INSERT INTO productos (categoria_id, nombre, descripcion, precio, disponible) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getCategoria().getId());
            stmt.setString(2, p.getNombre());
            stmt.setString(3, p.getDescripcion());
            stmt.setDouble(4, p.getPrecio());
            stmt.setBoolean(5, p.isDisponible());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET categoria_id=?, nombre=?, descripcion=?, precio=?, disponible=? WHERE id=?";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getCategoria().getId());
            stmt.setString(2, p.getNombre());
            stmt.setString(3, p.getDescripcion());
            stmt.setDouble(4, p.getPrecio());
            stmt.setBoolean(5, p.isDisponible());
            stmt.setInt(6, p.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        // INNER JOIN para traer la información de la categoría y no solo el ID
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, p.disponible, "
                + "c.id AS cat_id, c.nombre AS cat_nombre, c.descripcion AS cat_desc, c.activa AS cat_activa "
                + "FROM productos p INNER JOIN categorias c ON p.categoria_id = c.id";

        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria(
                        rs.getInt("cat_id"), rs.getString("cat_nombre"),
                        rs.getString("cat_desc"), rs.getBoolean("cat_activa")
                );

                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setDisponible(rs.getBoolean("disponible"));
                p.setCategoria(c);

                productos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }
}
