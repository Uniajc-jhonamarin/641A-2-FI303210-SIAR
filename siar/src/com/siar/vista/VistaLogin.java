package com.siar.vista;

import javax.swing.*;
import java.awt.*;

public class VistaLogin extends JFrame {
    
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public VistaLogin() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("SIAR - Inicio de Sesión");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        // Panel principal con márgenes
        JPanel panelPrincipal = new JPanel(new GridLayout(4, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Título
        JLabel lblTitulo = new JLabel("Bienvenido a SIAR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelPrincipal.add(lblTitulo);

        // Campo de Email
        JPanel panelEmail = new JPanel(new BorderLayout());
        panelEmail.add(new JLabel("Correo Electrónico:"), BorderLayout.NORTH);
        txtEmail = new JTextField();
        panelEmail.add(txtEmail, BorderLayout.CENTER);
        panelPrincipal.add(panelEmail);

        // Campo de Contraseña
        JPanel panelPassword = new JPanel(new BorderLayout());
        panelPassword.add(new JLabel("Contraseña:"), BorderLayout.NORTH);
        txtPassword = new JPasswordField();
        panelPassword.add(txtPassword, BorderLayout.CENTER);
        panelPrincipal.add(panelPassword);

        // Botón de Ingreso
        JPanel panelBoton = new JPanel(); // FlowLayout por defecto
        btnIngresar = new JButton("Ingresar al Sistema");
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBoton.add(btnIngresar);
        panelPrincipal.add(panelBoton);

        add(panelPrincipal);
    }

    // Métodos para que el Controlador pueda acceder a los datos
    public String getEmail() {
        return txtEmail.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
}