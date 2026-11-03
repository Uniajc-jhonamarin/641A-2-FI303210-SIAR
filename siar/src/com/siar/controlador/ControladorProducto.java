package com.siar.controlador;

import com.siar.modelo.Categoria;
import com.siar.modelo.Producto;
import com.siar.servicio.ProductoService;
import com.siar.vista.VistaProducto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ControladorProducto {

    private final VistaProducto vista;
    private final ProductoService servicio;
    private List<Producto> listaActual;

    public ControladorProducto(VistaProducto vista, ProductoService servicio) {
        this.vista = vista;
        this.servicio = servicio;

        cargarCategorias();
        cargarTabla();
        inicializarEventos();
    }

    private void cargarCategorias() {
        vista.getCbCategoria().removeAllItems();
        List<Categoria> categorias = servicio.obtenerCategoriasActivas();
        for (Categoria c : categorias) {
            vista.getCbCategoria().addItem(c);
        }
    }

    private void cargarTabla() {
        vista.getModeloTabla().setRowCount(0); // Limpiar tabla
        listaActual = servicio.obtenerTodosLosProductos();

        for (Producto p : listaActual) {
            vista.getModeloTabla().addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCategoria().getNombre(),
                "$" + p.getPrecio(),
                p.isDisponible() ? "Sí" : "No"
            });
        }
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

        // Evento para seleccionar una fila de la tabla con el mouse
        vista.getTablaProductos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.getTablaProductos().getSelectedRow();
                if (fila >= 0) {
                    cargarProductoEnFormulario(listaActual.get(fila));
                }
            }
        });
    }

    private void guardar() {
        Producto p = extraerDatosFormulario();
        if (p == null) {
            return;
        }

        if (servicio.guardarProducto(p)) {
            vista.mostrarMensaje("Producto guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        } else {
            vista.mostrarMensaje("Error al guardar. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        Producto p = extraerDatosFormulario();
        if (p == null || vista.getTxtId().getText().isEmpty()) {
            return;
        }

        p.setId(Integer.parseInt(vista.getTxtId().getText()));

        if (servicio.actualizarProducto(p)) {
            vista.mostrarMensaje("Producto actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        } else {
            vista.mostrarMensaje("Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        String idStr = vista.getTxtId().getText();
        if (idStr.isEmpty()) {
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Seguro que desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (servicio.eliminarProducto(Integer.parseInt(idStr))) {
                vista.mostrarMensaje("Producto eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTabla();
            } else {
                vista.mostrarMensaje("No se pudo eliminar. Puede estar asociado a un pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Producto extraerDatosFormulario() {
        try {
            Producto p = new Producto();
            p.setNombre(vista.getTxtNombre().getText().trim());
            p.setDescripcion(vista.getTxtDescripcion().getText().trim());
            p.setPrecio(Double.parseDouble(vista.getTxtPrecio().getText().trim()));
            p.setCategoria((Categoria) vista.getCbCategoria().getSelectedItem());
            p.setDisponible(vista.getChkDisponible().isSelected());
            return p;
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("El precio debe ser un número válido.", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void cargarProductoEnFormulario(Producto p) {
        vista.getTxtId().setText(String.valueOf(p.getId()));
        vista.getTxtNombre().setText(p.getNombre());
        vista.getTxtDescripcion().setText(p.getDescripcion());
        vista.getTxtPrecio().setText(String.valueOf(p.getPrecio()));
        vista.getChkDisponible().setSelected(p.isDisponible());

        // Seleccionar la categoría correcta en el combo
        for (int i = 0; i < vista.getCbCategoria().getItemCount(); i++) {
            if (vista.getCbCategoria().getItemAt(i).getId() == p.getCategoria().getId()) {
                vista.getCbCategoria().setSelectedIndex(i);
                break;
            }
        }

        vista.getBtnGuardar().setEnabled(false);
        vista.getBtnActualizar().setEnabled(true);
        vista.getBtnEliminar().setEnabled(true);
    }

    private void limpiarFormulario() {
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtDescripcion().setText("");
        vista.getTxtPrecio().setText("");
        if (vista.getCbCategoria().getItemCount() > 0) {
            vista.getCbCategoria().setSelectedIndex(0);
        }
        vista.getChkDisponible().setSelected(true);
        vista.getTablaProductos().clearSelection();

        vista.getBtnGuardar().setEnabled(true);
        vista.getBtnActualizar().setEnabled(false);
        vista.getBtnEliminar().setEnabled(false);
    }
}
