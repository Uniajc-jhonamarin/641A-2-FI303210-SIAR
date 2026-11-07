package com.siar.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaSeguimientoPedidos extends JPanel {

    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;

    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;

    private JLabel lblTotal;

    public VistaSeguimientoPedidos() {

        setLayout(new BorderLayout(10, 10));
        setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10));

        // =====================
        // PANEL IZQUIERDO
        // =====================

        JPanel panelIzquierdo =
                new JPanel(new BorderLayout());

        panelIzquierdo.setBorder(
                BorderFactory.createTitledBorder(
                        "Seguimiento de Pedidos"));

        modeloPedidos =
                new DefaultTableModel(
                        new String[]{
                            "ID",
                            "Mesa",
                            "Estado",
                            "Total"
                        }, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloPedidos);

        panelIzquierdo.add(
                new JScrollPane(tablaPedidos),
                BorderLayout.CENTER);

        // =====================
        // PANEL DERECHO
        // =====================

        JPanel panelDerecho =
                new JPanel(new BorderLayout());

        panelDerecho.setBorder(
                BorderFactory.createTitledBorder(
                        "Detalle del Pedido"));

        modeloDetalles =
                new DefaultTableModel(
                        new String[]{
                            "Cantidad",
                            "Producto",
                            "Subtotal"
                        }, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {
                return false;
            }
        };

        tablaDetalles =
                new JTable(modeloDetalles);

        panelDerecho.add(
                new JScrollPane(tablaDetalles),
                BorderLayout.CENTER);

        JPanel panelInferior =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT));

        lblTotal =
                new JLabel("Total: $0.00");

        lblTotal.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16));

        panelInferior.add(lblTotal);

        panelDerecho.add(
                panelInferior,
                BorderLayout.SOUTH);

        // =====================
        // SPLIT
        // =====================

        JSplitPane splitPane =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        panelIzquierdo,
                        panelDerecho);

        splitPane.setResizeWeight(0.60);

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

    public JLabel getLblTotal() {
        return lblTotal;
    }

    public void mostrarMensaje(
            String mensaje,
            String titulo,
            int tipo) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                titulo,
                tipo);
    }
}