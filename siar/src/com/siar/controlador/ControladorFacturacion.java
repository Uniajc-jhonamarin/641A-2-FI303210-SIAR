package com.siar.controlador;

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
        String filtro =
        vista.getCmbFiltroEstado()
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

    String estado =
        p.getEstadoActual().getNombre();

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

         int fila =
             vista.getTablaPedidos().getSelectedRow();

         if (fila == -1) {
             return;
         }

         Pedido p =
             pedidosVisibles.get(fila);

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
        "Pedido #" + p.getId() +
        " pagado y facturado con éxito.",
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
        vista.mostrarMensaje(
                "Seleccione un pedido.",
                "Atención",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {

        Pedido p = pedidosVisibles.get(fila);

        p.calcularTotal();

        double subtotal = p.getTotal();
        double iva = subtotal * 0.19;
        double total = subtotal + iva;

        String nombreArchivo =
                "comprobante_pedido_" + p.getId() + ".txt";

        FileWriter writer =
                new FileWriter(nombreArchivo);

        writer.write("=================================\n");
        writer.write("SIAR\n");
        writer.write("COMPROBANTE DE PAGO\n");
        writer.write("=================================\n\n");

        writer.write("Pedido: " + p.getId() + "\n");
        writer.write("Mesa: " + p.getNumeroMesa() + "\n\n");

        writer.write("Subtotal: $" + subtotal + "\n");
        writer.write("IVA (19%): $" + iva + "\n");
        writer.write("Descuento: $0\n");
        writer.write("TOTAL: $" + total + "\n\n");

        writer.write("Gracias por su compra.\n");

        writer.close();

        vista.mostrarMensaje(
                "Comprobante generado:\n"
                + nombreArchivo,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

    } catch (IOException e) {

        vista.mostrarMensaje(
                "Error al generar comprobante.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
    
}
