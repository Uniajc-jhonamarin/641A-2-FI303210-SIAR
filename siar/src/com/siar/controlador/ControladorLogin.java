package com.siar.controlador;

import com.siar.servicio.AuthService;
import com.siar.vista.VistaLogin;
import com.siar.vista.VistaDashboard;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPasswordField;

public class ControladorLogin {

    private final VistaLogin vista;
    private final AuthService authService;

    public ControladorLogin(VistaLogin vista, AuthService authService) {
        this.vista = vista;
        this.authService = authService;
        this.vista.getBtnRecuperar().addActionListener(e -> recuperarPassword());

        // Agregar el "escuchador" de eventos al botón de la vista
        this.vista.getBtnIngresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intentarLogin();
            }
        });
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    private void intentarLogin() {
        String email = vista.getEmail();
        String password = vista.getPassword();

        // 1. Validaciones básicas de formulario
        if (email.isEmpty() || password.isEmpty()) {
            vista.mostrarMensaje("Por favor, ingrese correo y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Llamada a la capa de negocio (Servicio)
        boolean exito = authService.iniciarSesion(email, password);

        // 3. Respuesta de la vista
        // 3. Respuesta de la vista
        if (exito) {
            vista.dispose(); // Cierra la ventana de login

            // Instanciar y abrir el Dashboard Principal
            VistaDashboard vistaDashboard = new VistaDashboard();
            ControladorDashboard ctrlDashboard = new ControladorDashboard(vistaDashboard);
            ctrlDashboard.iniciar();

        } else {
            vista.mostrarMensaje("Credenciales incorrectas o usuario inactivo.", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recuperarPassword() {
        String email = JOptionPane.showInputDialog(vista, "Ingrese su correo registrado:", "Recuperar Contraseña", JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.trim().isEmpty()) {
            return;
        }

        JPasswordField pwd = new JPasswordField();
        int opcion = JOptionPane.showConfirmDialog(vista, pwd, "Ingrese la nueva contraseña (mín. 4 caracteres):", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        String nuevaPass = new String(pwd.getPassword());
        if (authService.recuperarPassword(email, nuevaPass)) {
            vista.mostrarMensaje("Contraseña restablecida correctamente. Ya puede iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            vista.mostrarMensaje("No se pudo restablecer. Verifique el correo y que la contraseña tenga mínimo 4 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
