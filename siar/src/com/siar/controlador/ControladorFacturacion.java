package com.siar.controlador;

import com.siar.modelo.Factura;
import com.siar.servicio.ComprobanteService;
import com.siar.vista.VistaPreviaComprobante;
import java.awt.Window;
import java.io.File;

import com.siar.modelo.Pedido;
import com.siar.servicio.PedidoService;
import com.siar.vista.VistaFacturacion;

import javax.swing.*;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;

public class ControladorFacturacion {

    private final VistaFacturacion vista;
    private final PedidoService pedidoService;
    private List<Pedido> pedidosActivos;
    private List<Pedido> pedidosVisibles;
    private final ComprobanteService comprobanteService = new ComprobanteService();

    public ControladorFacturacion(VistaFacturacion vista, PedidoService pedidoService) {
        this.vista = vista;
        this.pedidoService = pedidoService;
        cargarPedidos();
        inicializarEventos();
    }

    private void cargarPedidos() {
        vista.getModeloPedidos().setRowCount(0);
        vista.getBtnEmitirComprobante().setEnabled(false);
        pedidosActivos = pedidoService.obtenerPedidosFacturacion();
        pedidosVisibles = new java.util.ArrayList<>();
        String filtro
                = vista.getCmbFiltroEstado()
                        .getSelectedItem()
                        .toString();
        if (filtro.equals("FACTURADOS")) {

            vista.getBtnFacturar()
                    .setEnabled(false);

        } else {

            vista.getBtnFacturar()
                    .setEnabled(true);
        }

        for (Pedido p : pedidosActivos) {

            String estado
                    = p.getEstadoActual().getNombre();

            if (filtro.equals("ENTREGADOS")
                    && !estado.equals("ENTREGADO")) {
                continue;
            }

            if (filtro.equals("FACTURADOS")
                    && !estado.equals("FACTURADO")) {
                continue;
            }

            vista.getModeloPedidos().addRow(
                    new Object[]{
                        p.getId(),
                        "Mesa " + p.getNumeroMesa(),
                        estado
                    });
            pedidosVisibles.add(p);
        }
    }

    private void inicializarEventos() {

        vista.getBtnFacturar()
                .addActionListener(e -> facturarPedido());

        vista.getBtnEmitirComprobante()
                .addActionListener(e -> emitirComprobante());

        vista.getCmbFiltroEstado()
                .addActionListener(
                        e -> cargarPedidos()
                );

        vista.getTablaPedidos()
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    int fila
                            = vista.getTablaPedidos().getSelectedRow();

                    if (fila == -1) {
                        return;
                    }

                    Pedido p
                            = pedidosVisibles.get(fila);

                    if (p.getEstadoActual()
                            .getNombre()
                            .equals("FACTURADO")) {

                        vista.getBtnEmitirComprobante()
                                .setEnabled(true);

                    } else {

                        vista.getBtnEmitirComprobante()
                                .setEnabled(false);
                    }
                });

    }

    private void facturarPedido() {
        int fila = vista.getTablaPedidos().getSelectedRow();
        if (fila == -1) {
            vista.mostrarMensaje("Seleccione un pedido para cobrar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pedido p = pedidosVisibles.get(fila);

        System.out.println("Pedido: " + p.getId());
        System.out.println("Detalles: " + p.getDetalles().size());

        p.calcularTotal();
        System.out.println("Total: " + p.getTotal());

        p.calcularTotal();

        double subtotal = p.getTotal();

        vista.mostrarFactura(
                p.getId(),
                p.getNumeroMesa(),
                subtotal,
                0
        );

        // Por regla de negocio (Patrón State), solo cobramos si ya está ENTREGADO
        if (!p.getEstadoActual().getNombre().equals("ENTREGADO")) {
            vista.mostrarMensaje("El pedido aún está en: " + p.getEstadoActual().getNombre() + ".\nDebe ser ENTREGADO antes de facturar.", "Ciclo incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Avanza de ENTREGADO -> FACTURADO
        if (pedidoService.avanzarEstado(p)) {

            vista.mostrarMensaje(
                    "Pedido #" + p.getId()
                    + " pagado y facturado con éxito.",
                    "Cobro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            cargarPedidos();
        } else {
            vista.mostrarMensaje("Error al procesar la factura.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void emitirComprobante() {
        int fila = vista.getTablaPedidos().getSelectedRow();
        if (fila == -1) {
            vista.mostrarMensaje("Seleccione un pedido.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Pedido p = pedidosVisibles.get(fila);

            // 1. Construir la Factura usando el modelo y las reglas de negocio
            Factura factura = comprobanteService.construirFactura(p);

            // 2. Generar HTML y guardarlo en la carpeta comprobantes/
            String html = comprobanteService.generarHtmlComprobante(p, factura);
            File archivo = comprobanteService.guardarComprobanteHtml(p.getId(), html);

            // 3. Mostrar la vista previa profesional con opción de Imprimir / PDF
            Window ventanaPadre = SwingUtilities.getWindowAncestor(vista);
            VistaPreviaComprobante preview = new VistaPreviaComprobante(ventanaPadre, html, archivo);
            preview.setVisible(true);

        } catch (IOException e) {
            vista.mostrarMensaje(
                    "Error al generar comprobante: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
