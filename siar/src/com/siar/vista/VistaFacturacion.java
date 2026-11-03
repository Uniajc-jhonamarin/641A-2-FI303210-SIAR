package com.siar.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaFacturacion extends JPanel {

    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;
    private JButton btnFacturar;

    public VistaFacturacion() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel Central: Tabla de pedidos
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Caja - Pedidos Activos"));

        modeloPedidos = new DefaultTableModel(new String[]{"ID Pedido", "Mesa", "Estado Actual"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelCentral.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        // Panel Sur: Botón de cobro
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFacturar = new JButton("Cobrar y Facturar Pedido");
        btnFacturar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFacturar.setBackground(new Color(0, 102, 204));
        btnFacturar.setForeground(Color.WHITE);
        panelSur.add(btnFacturar);

        add(panelCentral, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
    }

    public JTable getTablaPedidos() {
        return tablaPedidos;
    }

    public DefaultTableModel getModeloPedidos() {
        return modeloPedidos;
    }

    public JButton getBtnFacturar() {
        return btnFacturar;
    }

    public void mostrarMensaje(String msj, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msj, titulo, tipo);
    }
}
