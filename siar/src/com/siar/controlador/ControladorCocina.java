package com.siar.controlador;

import com.siar.modelo.DetallePedido;
import com.siar.modelo.Pedido;
import com.siar.servicio.PedidoService;
import com.siar.vista.VistaCocina;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ControladorCocina {

    private final VistaCocina vista;
    private final PedidoService pedidoService;
    private List<Pedido> pedidosActivos;
    private Pedido pedidoSeleccionado;

    public ControladorCocina(VistaCocina vista, PedidoService pedidoService) {
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
        limpiarDetalles();
    }

    private void inicializarEventos() {
        vista.getTablaPedidos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarPedido();
            }
        });

        vista.getBtnAvanzarEstado().addActionListener(e -> avanzarEstado());
    }

    private void seleccionarPedido() {
        int fila = vista.getTablaPedidos().getSelectedRow();
        if (fila == -1) {
            return;
        }

        pedidoSeleccionado = pedidosActivos.get(fila);

        // Cargar los productos a cocinar
        vista.getModeloDetalles().setRowCount(0);
        List<DetallePedido> detalles = pedidoService.obtenerDetalles(pedidoSeleccionado.getId());
        for (DetallePedido dp : detalles) {
            vista.getModeloDetalles().addRow(new Object[]{dp.getCantidad(), dp.getProducto().getNombre()});
        }

        // Configurar el botón según el Patrón State
        vista.getBtnAvanzarEstado().setEnabled(true);
        vista.getBtnAvanzarEstado().setText("Avanzar: Pasar al siguiente estado");
    }

    private void avanzarEstado() {
        if (pedidoSeleccionado != null) {
            if (pedidoService.avanzarEstado(pedidoSeleccionado)) {
                cargarPedidos();
            } else {
                vista.mostrarMensaje("Error al actualizar el estado en BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limpiarDetalles() {
        if (vista.getModeloDetalles() != null) {
            vista.getModeloDetalles().setRowCount(0);
        }
        vista.getBtnAvanzarEstado().setEnabled(false);
        vista.getBtnAvanzarEstado().setText("Seleccione un pedido");
    }
    
}
