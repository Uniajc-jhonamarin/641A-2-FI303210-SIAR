package com.siar.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaCocina extends JPanel {

    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;
    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;
    private JButton btnAvanzarEstado;
    
    private JLabel lblPendientes;
    private JLabel lblPreparacion;
    private JLabel lblListos;
    private JLabel lblEntregados;
    
    public VistaCocina() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelIndicadores = new JPanel(
            new GridLayout(1, 4, 10, 10));

        lblPendientes = new JLabel("PENDIENTES: 0",
            SwingConstants.CENTER);

        lblPreparacion = new JLabel("PREPARANDO: 0",
            SwingConstants.CENTER);

        lblListos = new JLabel("LISTOS: 0",
            SwingConstants.CENTER);

        lblEntregados = new JLabel("ENTREGADOS: 0",
            SwingConstants.CENTER);

        panelIndicadores.add(lblPendientes);
        panelIndicadores.add(lblPreparacion);
        panelIndicadores.add(lblListos);
        panelIndicadores.add(lblEntregados);

        add(panelIndicadores, BorderLayout.NORTH);

        // Panel Izquierdo: Lista de Pedidos
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Pedidos Activos"));

        modeloPedidos = new DefaultTableModel(new String[]{"ID Pedido", "Mesa", "Estado Actual"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelIzquierdo.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        // Panel Derecho: Detalles e Interacción
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Detalle a Preparar"));

        modeloDetalles = new DefaultTableModel(new String[]{"Cantidad", "Producto"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDetalles = new JTable(modeloDetalles);
        panelDerecho.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        btnAvanzarEstado = new JButton("Seleccione un pedido");
        btnAvanzarEstado.setFont(new Font("Arial", Font.BOLD, 16));
        btnAvanzarEstado.setEnabled(false);
        panelDerecho.add(btnAvanzarEstado, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelIzquierdo,
            panelDerecho);

        splitPane.setResizeWeight(0.50);

        add(splitPane, BorderLayout.CENTER);
    }

    public JTable getTablaPedidos() {
        return tablaPedidos;
    }

    public DefaultTableModel getModeloPedidos() {
        return modeloPedidos;
    }

    public JTable getTablaDetalles() {
        return tablaDetalles;
    }

    public DefaultTableModel getModeloDetalles() {
        return modeloDetalles;
    }

    public JButton getBtnAvanzarEstado() {
        return btnAvanzarEstado;
    }

    public void mostrarMensaje(String msj, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msj, titulo, tipo);
    }
    public JLabel getLblPendientes() {
    return lblPendientes;
    }

    public JLabel getLblPreparacion() {
    return lblPreparacion;
    }

    public JLabel getLblListos() {
    return lblListos;
    }

    public JLabel getLblEntregados() {
    return lblEntregados;
    }
}
