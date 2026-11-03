package com.siar.controlador;

import com.siar.modelo.Pedido;
import com.siar.servicio.PedidoService;
import com.siar.vista.VistaFacturacion;

import javax.swing.*;
import java.util.List;

public class ControladorFacturacion {

    private final VistaFacturacion vista;
    private final PedidoService pedidoService;
    private List<Pedido> pedidosActivos;

    public ControladorFacturacion(VistaFacturacion vista, PedidoService pedidoService) {
        this.vista = vista;
        this.pedidoService = pedidoService;
        cargarPedidos();
        inicializarEventos();
    }

    private void cargarPedidos() {
        vista.getModeloPedidos().setRowCount(0);
        pedidosActivos = pedidoService.obtenerPedidosPendientes();

        for (Pedido p : pedidosActivos) {
            vista.getModeloPedidos().addRow(new Object[]{
                p.getId(), "Mesa " + p.getNumeroMesa(), p.getEstadoActual().getNombre()
            });
        }
    }

    private void inicializarEventos() {
        vista.getBtnFacturar().addActionListener(e -> facturarPedido());
    }

    private void facturarPedido() {
        int fila = vista.getTablaPedidos().getSelectedRow();
        if (fila == -1) {
            vista.mostrarMensaje("Seleccione un pedido para cobrar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pedido p = pedidosActivos.get(fila);

        // Por regla de negocio (Patrón State), solo cobramos si ya está ENTREGADO
        if (!p.getEstadoActual().getNombre().equals("ENTREGADO")) {
            vista.mostrarMensaje("El pedido aún está en: " + p.getEstadoActual().getNombre() + ".\nDebe ser ENTREGADO antes de facturar.", "Ciclo incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Avanza de ENTREGADO -> FACTURADO
        if (pedidoService.avanzarEstado(p)) {
            vista.mostrarMensaje("Pedido #" + p.getId() + " pagado y facturado con éxito.", "Cobro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            cargarPedidos(); // Recarga la tabla y el pedido desaparecerá
        } else {
            vista.mostrarMensaje("Error al procesar la factura.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
