package com.siar.repositorio;

import com.siar.modelo.DetallePedido;
import com.siar.modelo.Pedido;
import java.util.List;

public interface PedidoRepository {

    boolean guardarPedido(Pedido pedido);

    boolean actualizarEstado(int idPedido, String nuevoEstado);

    List<Pedido> obtenerPedidosActivos(); // Para la cocina (KDS) y meseros

    List<DetallePedido> obtenerDetalles(int idPedido);

}
