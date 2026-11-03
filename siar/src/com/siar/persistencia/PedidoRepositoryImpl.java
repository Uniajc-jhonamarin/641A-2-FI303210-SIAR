package com.siar.persistencia;

import com.siar.configuracion.ConexionBaseDatos;
import com.siar.modelo.DetallePedido;
import com.siar.modelo.Pedido;
import com.siar.repositorio.PedidoRepository;
import com.siar.seguridad.SesionActual;
import com.siar.patrones.estado.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepositoryImpl implements PedidoRepository {

    @Override
    public boolean guardarPedido(Pedido pedido) {
        // 1. Adaptamos el SQL a las columnas exactas de tu tabla pedidos
        String sqlPedido = "INSERT INTO pedidos (mesa_id, mesero_id, cliente_id, estado) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalles_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConexionBaseDatos.getInstancia().getConexion();
            conn.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                // Convertimos el texto que escribe el usuario en la vista a número (Ej: Mesa "1")
                stmtPedido.setInt(1, Integer.parseInt(pedido.getNumeroMesa()));

                // Usamos nuestro Singleton para saber quién está registrando el pedido
                stmtPedido.setInt(2, SesionActual.getInstancia().getUsuarioActual().getId());

                // Como aún no tenemos módulo de clientes, enviamos NULL por ahora
                stmtPedido.setNull(3, java.sql.Types.INTEGER);

                // Estado inicial
                stmtPedido.setString(4, pedido.getEstadoActual().getNombre());

                stmtPedido.executeUpdate();

                try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el ID del pedido.");
                    }
                }
            }

            try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                for (DetallePedido dp : pedido.getDetalles()) {
                    stmtDetalle.setInt(1, pedido.getId());
                    stmtDetalle.setInt(2, dp.getProducto().getId());
                    stmtDetalle.setInt(3, dp.getCantidad());
                    stmtDetalle.setDouble(4, dp.getProducto().getPrecio());
                    stmtDetalle.setDouble(5, dp.getSubtotal());
                    stmtDetalle.addBatch();
                }
                stmtDetalle.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error en la transacción del pedido: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idPedido);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Pedido> obtenerPedidosActivos() {
        List<Pedido> pedidos = new ArrayList<>();
        // Actualizamos el SELECT para que coincida con tus columnas reales
        String sql = "SELECT id, mesa_id, estado, fecha_creacion FROM pedidos WHERE estado NOT IN ('FACTURADO', 'CANCELADO')";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id"));
                p.setNumeroMesa(String.valueOf(rs.getInt("mesa_id")));

                String estadoBd = rs.getString("estado");
                switch (estadoBd) {
                    case "EN_PREPARACION":
                        p.setEstadoActual(new EstadoPreparacion());
                        break;
                    case "LISTO":
                        p.setEstadoActual(new EstadoListo());
                        break;
                    case "ENTREGADO":
                        p.setEstadoActual(new EstadoEntregado());
                        break;
                    case "FACTURADO":
                        p.setEstadoActual(new EstadoFacturado());
                        break;
                    default:
                        p.setEstadoActual(new EstadoPendiente());
                        break;
                }
                pedidos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    @Override
    public List<DetallePedido> obtenerDetalles(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT dp.cantidad, p.nombre FROM detalles_pedido dp INNER JOIN productos p ON dp.producto_id = p.id WHERE dp.pedido_id = ?";
        try (Connection conn = ConexionBaseDatos.getInstancia().getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    com.siar.modelo.Producto prod = new com.siar.modelo.Producto();
                    prod.setNombre(rs.getString("nombre"));
                    DetallePedido dp = new DetallePedido();
                    dp.setProducto(prod);
                    dp.setCantidad(rs.getInt("cantidad"));
                    detalles.add(dp);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles: " + e.getMessage());
        }
        return detalles;
    }

}
