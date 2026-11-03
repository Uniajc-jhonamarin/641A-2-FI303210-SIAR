package com.siar.controlador;

import com.siar.modelo.DetallePedido;
import com.siar.modelo.Pedido;
import com.siar.modelo.Producto;
import com.siar.servicio.PedidoService;
import com.siar.servicio.ProductoService;
import com.siar.vista.VistaPedido;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ControladorPedido {

    private final VistaPedido vista;
    private final PedidoService pedidoService;
    private final ProductoService productoService;

    private List<Producto> catalogoProductos;
    private Pedido pedidoActual;

    public ControladorPedido(VistaPedido vista, PedidoService pedidoService, ProductoService productoService) {
        this.vista = vista;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.pedidoActual = new Pedido();

        cargarCatalogo();
        inicializarEventos();
    }

    private void cargarCatalogo() {
        catalogoProductos = productoService.obtenerTodosLosProductos();
        vista.getModeloProductos().setRowCount(0);

        for (Producto p : catalogoProductos) {
            if (p.isDisponible()) { // Solo mostramos los disponibles
                vista.getModeloProductos().addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getPrecio()
                });
            }
        }
    }

    private void inicializarEventos() {
        vista.getBtnAgregar().addActionListener(e -> agregarAlCarrito());
        vista.getBtnEliminarItem().addActionListener(e -> eliminarDelCarrito());
        vista.getBtnConfirmarPedido().addActionListener(e -> guardarPedido());
    }

    private void agregarAlCarrito() {
        int filaSelec = vista.getTablaProductos().getSelectedRow();
        if (filaSelec == -1) {
            vista.mostrarMensaje("Seleccione un producto del menú.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener el ID del producto desde la tabla y buscarlo en la lista
        int idProd = (int) vista.getModeloProductos().getValueAt(filaSelec, 0);
        Producto prodSeleccionado = catalogoProductos.stream()
                .filter(p -> p.getId() == idProd)
                .findFirst().orElse(null);

        if (prodSeleccionado != null) {
            int cantidad = (int) vista.getSpinCantidad().getValue();
            DetallePedido detalle = new DetallePedido(prodSeleccionado, cantidad);

            pedidoActual.agregarDetalle(detalle);
            actualizarVistaCarrito();
            vista.getSpinCantidad().setValue(1); // Reiniciar el spinner
        }
    }

    private void eliminarDelCarrito() {
        int filaSelec = vista.getTablaCarrito().getSelectedRow();
        if (filaSelec == -1) {
            vista.mostrarMensaje("Seleccione un ítem del carrito para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        pedidoActual.getDetalles().remove(filaSelec);
        pedidoActual.calcularTotal();
        actualizarVistaCarrito();
    }

    private void actualizarVistaCarrito() {
        vista.getModeloCarrito().setRowCount(0);
        for (DetallePedido dp : pedidoActual.getDetalles()) {
            vista.getModeloCarrito().addRow(new Object[]{
                dp.getProducto().getId(),
                dp.getProducto().getNombre(),
                dp.getCantidad(),
                dp.getSubtotal()
            });
        }
        vista.getLblTotal().setText("Total: $" + pedidoActual.getTotal());
    }

    private void guardarPedido() {
        String mesa = vista.getTxtMesa().getText().trim();
        if (mesa.isEmpty()) {
            vista.mostrarMensaje("Debe ingresar un número de mesa.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pedidoActual.getDetalles().isEmpty()) {
            vista.mostrarMensaje("El pedido no tiene productos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pedidoActual.setNumeroMesa(mesa);
        pedidoActual.setNombreCliente(vista.getTxtCliente().getText().trim());

        if (pedidoService.crearPedido(pedidoActual)) {
            vista.mostrarMensaje("¡Pedido registrado con éxito y enviado a cocina!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            reiniciarPedido();
        } else {
            vista.mostrarMensaje("Error al guardar el pedido en la base de datos.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reiniciarPedido() {
        pedidoActual = new Pedido();
        vista.getTxtMesa().setText("");
        vista.getTxtCliente().setText("");
        actualizarVistaCarrito();
    }
}
