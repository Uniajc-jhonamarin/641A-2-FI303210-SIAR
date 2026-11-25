package com.siar.servicio;

import com.siar.modelo.Pedido;
import com.siar.repositorio.PedidoRepository;
import com.siar.patrones.observador.ObservadorPedido;

import java.util.List;
import java.util.ArrayList;

public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final List<ObservadorPedido> observadores = new ArrayList<>();

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
        
        boolean guardado = pedidoRepo.guardarPedido(pedido);
        if (guardado) {
            notificarObservadores(pedido);
        }
        return guardado;
    }

    public boolean avanzarEstado(Pedido pedido) {
        if (pedido == null || pedido.getId() <= 0) {
            return false;
        }

        // 1. Actualizamos lógicamente el objeto usando el Patrón State
        pedido.procesarSiguienteEstado();
        
        boolean actualizado = pedidoRepo.actualizarEstado(pedido.getId(), pedido.getEstadoActual().getNombre());
        if (actualizado) {
            notificarObservadores(pedido);
        }
        return actualizado;
    }

    public List<Pedido> obtenerPedidosPendientes() {
        return pedidoRepo.obtenerPedidosActivos();
    }

    public List<Pedido> obtenerPedidosFacturacion() {
        return pedidoRepo.obtenerPedidosFacturacion();
    }

    public List<com.siar.modelo.DetallePedido> obtenerDetalles(int idPedido) {
        return pedidoRepo.obtenerDetalles(idPedido);
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepo.obtenerTodosLosPedidos();
    }

    public void agregarObservador(ObservadorPedido observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void eliminarObservador(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    public void notificarObservadores(Pedido pedido) {
        for (ObservadorPedido obs : observadores) {
            obs.actualizar(pedido);
        }
    }
}
