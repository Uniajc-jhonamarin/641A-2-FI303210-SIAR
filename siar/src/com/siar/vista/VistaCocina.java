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

    public VistaCocina() {
        setLayout(new GridLayout(1, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        add(panelIzquierdo);
        add(panelDerecho);
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
}
