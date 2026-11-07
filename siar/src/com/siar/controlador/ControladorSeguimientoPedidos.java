package com.siar.controlador;

import com.siar.modelo.DetallePedido;
import com.siar.modelo.Pedido;
import com.siar.servicio.PedidoService;
import com.siar.vista.VistaSeguimientoPedidos;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ControladorSeguimientoPedidos {

    private final VistaSeguimientoPedidos vista;
    private final PedidoService pedidoService;

    private List<Pedido> pedidos;

    public ControladorSeguimientoPedidos(
            VistaSeguimientoPedidos vista,
            PedidoService pedidoService) {

        this.vista = vista;
        this.pedidoService = pedidoService;

        cargarPedidos();
        inicializarEventos();
    }

    private void cargarPedidos() {

        vista.getModeloPedidos().setRowCount(0);

        pedidos =pedidoService.obtenerTodosLosPedidos();

        for (Pedido p : pedidos) {

            p.calcularTotal();

            vista.getModeloPedidos().addRow(
                    new Object[]{
                        p.getId(),
                        p.getNumeroMesa(),
                        p.getEstadoActual().getNombre(),
                        p.getTotal()
                    });
        }
    }

    private void inicializarEventos() {

        vista.getTablaPedidos()
                .addMouseListener(
                        new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                mostrarDetallePedido();
            }
        });
    }

    private void mostrarDetallePedido() {

        int fila =
                vista.getTablaPedidos()
                     .getSelectedRow();

        if (fila == -1) {
            return;
        }

        Pedido pedido = pedidos.get(fila);

        vista.getModeloDetalles()
             .setRowCount(0);

        List<DetallePedido> detalles =
                pedidoService.obtenerDetalles(
                        pedido.getId());

        double total = 0;

        for (DetallePedido dp : detalles) {

            vista.getModeloDetalles()
                 .addRow(
                    new Object[]{
                        dp.getCantidad(),
                        dp.getProducto().getNombre(),
                        dp.getSubtotal()
                    });

            total += dp.getSubtotal();
        }

        vista.getLblTotal()
             .setText("Total: $" + total);
    }
}