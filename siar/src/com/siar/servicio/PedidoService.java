package com.siar.servicio;

import com.siar.modelo.Pedido;
import com.siar.repositorio.PedidoRepository;

import java.util.List;

public class PedidoService {

    private final PedidoRepository pedidoRepo;

    public PedidoService(PedidoRepository pedidoRepo) {
        this.pedidoRepo = pedidoRepo;
    }

    public boolean crearPedido(Pedido pedido) {
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            return false; // No se puede crear un pedido vacío
        }
        if (pedido.getNumeroMesa() == null || pedido.getNumeroMesa().trim().isEmpty()) {
            return false;
        }

        pedido.calcularTotal(); // Asegurar que el total esté actualizado
        return pedidoRepo.guardarPedido(pedido);
    }

    public boolean avanzarEstado(Pedido pedido) {
        if (pedido == null || pedido.getId() <= 0) {
            return false;
        }

        // 1. Actualizamos lógicamente el objeto usando el Patrón State
        pedido.procesarSiguienteEstado();

        // 2. Persistimos el nuevo estado en la base de datos
        return pedidoRepo.actualizarEstado(pedido.getId(), pedido.getEstadoActual().getNombre());
    }

    public List<Pedido> obtenerPedidosPendientes() {
        return pedidoRepo.obtenerPedidosActivos();
    }

    public List<com.siar.modelo.DetallePedido> obtenerDetalles(int idPedido) {
        return pedidoRepo.obtenerDetalles(idPedido);
    }
}
