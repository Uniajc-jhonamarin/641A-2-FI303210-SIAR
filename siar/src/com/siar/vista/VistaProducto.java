package com.siar.vista;

import com.siar.modelo.Categoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaProducto extends JPanel {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    private JTextField txtId, txtNombre, txtDescripcion, txtPrecio;
    private JComboBox<Categoria> cbCategoria;
    private JCheckBox chkDisponible;

    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;

    public VistaProducto() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PANEL IZQUIERDO: Formulario ---
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 5, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        panelFormulario.setPreferredSize(new Dimension(300, 250));

        panelFormulario.add(new JLabel("ID (Automático):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Categoría:"));
        cbCategoria = new JComboBox<>();
        panelFormulario.add(cbCategoria);

        panelFormulario.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        panelFormulario.add(txtDescripcion);

        panelFormulario.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Estado:"));
        chkDisponible = new JCheckBox("Disponible para venta", true);
        panelFormulario.add(chkDisponible);

        // Botones del formulario
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Contenedor para el formulario y botones
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelFormulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        add(panelIzquierdo, BorderLayout.WEST);

        // --- PANEL DERECHO: Tabla ---
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Categoría", "Precio", "Disponible"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } // Tabla de solo lectura
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));
        add(scrollTabla, BorderLayout.CENTER);
    }

    // Getters para el Controlador
    public JTable getTablaProductos() {
        return tablaProductos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JComboBox<Categoria> getCbCategoria() {
        return cbCategoria;
    }

    public JTextField getTxtDescripcion() {
        return txtDescripcion;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public JCheckBox getChkDisponible() {
        return chkDisponible;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public void mostrarMensaje(String msj, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msj, titulo, tipo);
    }
}
