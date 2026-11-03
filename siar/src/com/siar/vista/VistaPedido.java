package com.siar.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaPedido extends JPanel {

    // Panel Izquierdo: Catálogo de Productos
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;
    private JSpinner spinCantidad;
    private JButton btnAgregar;

    // Panel Derecho: Ticket / Carrito
    private JTextField txtMesa;
    private JTextField txtCliente;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblTotal;
    private JButton btnEliminarItem;
    private JButton btnConfirmarPedido;

    public VistaPedido() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==========================================
        // PANEL IZQUIERDO: Catálogo de Productos
        // ==========================================
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Menú Disponible"));
        panelIzquierdo.setPreferredSize(new Dimension(400, 0));

        modeloProductos = new DefaultTableModel(new String[]{"ID", "Producto", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelIzquierdo.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAgregar.add(new JLabel("Cantidad:"));
        spinCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        panelAgregar.add(spinCantidad);
        btnAgregar = new JButton("Agregar al Pedido >>");
        panelAgregar.add(btnAgregar);
        panelIzquierdo.add(panelAgregar, BorderLayout.SOUTH);

        add(panelIzquierdo, BorderLayout.WEST);

        // ==========================================
        // PANEL DERECHO: Carrito / Ticket
        // ==========================================
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Detalle del Pedido Actual"));

        // Cabecera del pedido (Mesa y Cliente)
        JPanel panelCabecera = new JPanel(new GridLayout(2, 2, 5, 5));
        panelCabecera.add(new JLabel("Número de Mesa:"));
        txtMesa = new JTextField();
        panelCabecera.add(txtMesa);
        panelCabecera.add(new JLabel("Nombre del Cliente (Opcional):"));
        txtCliente = new JTextField();
        panelCabecera.add(txtCliente);
        panelDerecho.add(panelCabecera, BorderLayout.NORTH);

        // Tabla del carrito
        modeloCarrito = new DefaultTableModel(new String[]{"ID Prod", "Producto", "Cant", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelDerecho.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        // Pie del pedido (Total y Botones)
        JPanel panelPie = new JPanel(new BorderLayout());

        JPanel panelAccionesCarrito = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnEliminarItem = new JButton("Quitar Item");
        panelAccionesCarrito.add(btnEliminarItem);
        panelPie.add(panelAccionesCarrito, BorderLayout.WEST);

        JPanel panelConfirmacion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        panelConfirmacion.add(lblTotal);

        btnConfirmarPedido = new JButton("CONFIRMAR PEDIDO");
        btnConfirmarPedido.setBackground(new Color(34, 139, 34)); // Verde
        btnConfirmarPedido.setForeground(Color.WHITE);
        panelConfirmacion.add(btnConfirmarPedido);
        panelPie.add(panelConfirmacion, BorderLayout.EAST);

        panelDerecho.add(panelPie, BorderLayout.SOUTH);

        add(panelDerecho, BorderLayout.CENTER);
    }

    // Getters
    public JTable getTablaProductos() {
        return tablaProductos;
    }

    public DefaultTableModel getModeloProductos() {
        return modeloProductos;
    }

    public JSpinner getSpinCantidad() {
        return spinCantidad;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JTextField getTxtMesa() {
        return txtMesa;
    }

    public JTextField getTxtCliente() {
        return txtCliente;
    }

    public JTable getTablaCarrito() {
        return tablaCarrito;
    }

    public DefaultTableModel getModeloCarrito() {
        return modeloCarrito;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public JButton getBtnEliminarItem() {
        return btnEliminarItem;
    }

    public JButton getBtnConfirmarPedido() {
        return btnConfirmarPedido;
    }

    public void mostrarMensaje(String msj, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msj, titulo, tipo);
    }
}
