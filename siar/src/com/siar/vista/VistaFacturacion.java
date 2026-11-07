package com.siar.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaFacturacion extends JPanel {

    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;
    private JButton btnFacturar;
    private JComboBox<String> cmbFiltroEstado;
    
    private JLabel lblPedido;
    private JLabel lblMesa;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblDescuento;
    private JLabel lblPorcentajeDescuento;
    private JLabel lblTotal;

    private JButton btnEmitirComprobante;

    public VistaFacturacion() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel Central: Tabla de pedidos
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Caja y Facturación"));
        
        JPanel panelSuperior = new JPanel(
        new FlowLayout(FlowLayout.LEFT));

        panelSuperior.add(new JLabel("Filtro:"));

        cmbFiltroEstado = new JComboBox<>(
            new String[]{
            "ENTREGADOS",
            "FACTURADOS",
            "TODOS"
        });

        panelSuperior.add(cmbFiltroEstado);

        panelCentral.add(panelSuperior,
            BorderLayout.NORTH);
        
        modeloPedidos = new DefaultTableModel(new String[]{"ID Pedido", "Mesa", "Estado Actual"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelCentral.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);
        
        JPanel panelFactura = new JPanel();
        panelFactura.setLayout(new GridLayout(8, 1, 5, 5));
        panelFactura.setBorder(
            BorderFactory.createTitledBorder("Factura")
        );

        lblPedido = new JLabel("Pedido: -");
        lblMesa = new JLabel("Mesa: -");
        lblSubtotal = new JLabel("Subtotal: $0");
        lblIva = new JLabel("IVA (19%): $0");
        lblDescuento = new JLabel("Descuento: $0");
        lblPorcentajeDescuento = new JLabel("Descuento Aplicado: 0%");
        lblTotal = new JLabel("Total: $0");

        btnEmitirComprobante = new JButton("Emitir Comprobante");
        btnEmitirComprobante.setEnabled(false);

        panelFactura.add(lblPedido);
        panelFactura.add(lblMesa);
        panelFactura.add(lblSubtotal);
        panelFactura.add(lblIva);
        panelFactura.add(lblDescuento);
        panelFactura.add(lblPorcentajeDescuento);
        panelFactura.add(lblTotal);
        panelFactura.add(btnEmitirComprobante);

        // Panel Sur: Botón de cobro
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFacturar = new JButton("Cobrar y Facturar Pedido");
        btnFacturar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFacturar.setBackground(new Color(0, 102, 204));
        btnFacturar.setForeground(Color.WHITE);
        panelSur.add(btnFacturar);

        JSplitPane splitPane = new JSplitPane(
        JSplitPane.HORIZONTAL_SPLIT,
        panelCentral,
        panelFactura);

        splitPane.setResizeWeight(0.70);

        add(splitPane, BorderLayout.CENTER);
        
        add(panelSur, BorderLayout.SOUTH);
    }

    public JTable getTablaPedidos() {
        return tablaPedidos;
    }

    public DefaultTableModel getModeloPedidos() {
        return modeloPedidos;
    }

    public JButton getBtnEmitirComprobante() {
    return btnEmitirComprobante;
    }
    
    public JButton getBtnFacturar() {
        return btnFacturar;
    }
    
    public JComboBox<String> getCmbFiltroEstado() {
    return cmbFiltroEstado;
    }

    public void mostrarMensaje(String msj, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msj, titulo, tipo);
    }


    public void mostrarFactura(
        int pedido,
        String mesa,
        double subtotal,
        double descuento) {

    double iva = subtotal * 0.19;

    double descuentoAplicado = 0;
    double porcentaje = 0;

    if (subtotal >= 50000) {
        porcentaje = 10;
        descuentoAplicado = subtotal * 0.10;
    }

    double total = subtotal + iva - descuentoAplicado;

    lblPedido.setText("Pedido: " + pedido);
    lblMesa.setText("Mesa: " + mesa);

    lblSubtotal.setText("Subtotal: $" + subtotal);
    lblIva.setText("IVA (19%): $" + iva);

    lblDescuento.setText(
            "Descuento: $" + descuentoAplicado);

    lblPorcentajeDescuento.setText(
            "Descuento Aplicado: " + porcentaje + "%");

    lblTotal.setText("Total: $" + total);
}
}
