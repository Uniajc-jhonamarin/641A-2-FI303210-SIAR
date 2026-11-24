package com.siar.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;

public class VistaPreviaComprobante extends JDialog {

    private final JEditorPane editorPane;
    private final File archivoHtml;

    public VistaPreviaComprobante(Window owner, String contenidoHtml, File archivoHtml) {
        super(owner, "Vista Previa - Comprobante de Pago", ModalityType.APPLICATION_MODAL);
        this.archivoHtml = archivoHtml;

        setSize(480, 620);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Visor HTML nativo de Swing
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html; charset=UTF-8");
        editorPane.setText(contenidoHtml);
        editorPane.setEditable(false);
        editorPane.setCaretPosition(0);

        add(new JScrollPane(editorPane), BorderLayout.CENTER);

        // Barra inferior de acciones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnImprimir = new JButton("Imprimir / Guardar PDF");
        btnImprimir.setBackground(new Color(0, 102, 204));
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.addActionListener(e -> imprimirComprobante());

        JButton btnAbrirNavegador = new JButton("Abrir en Navegador");
        btnAbrirNavegador.addActionListener(e -> abrirEnNavegador());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnAbrirNavegador);
        panelBotones.add(btnImprimir);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void imprimirComprobante() {
        try {
            boolean completo = editorPane.print();
            if (completo) {
                JOptionPane.showMessageDialog(this, "Comprobante enviado a impresión.", "Impresión", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirEnNavegador() {
        try {
            if (Desktop.isDesktopSupported() && archivoHtml != null) {
                Desktop.getDesktop().browse(archivoHtml.toURI());
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir en el navegador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}