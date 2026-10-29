package com.siar.controlador;

import com.siar.servicio.AuthService;
import com.siar.vista.VistaLogin;
import com.siar.seguridad.SesionActual;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorLogin {
    
    private final VistaLogin vista;
    private final AuthService authService;

    public ControladorLogin(VistaLogin vista, AuthService authService) {
        this.vista = vista;
        this.authService = authService;
        
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
        if (exito) {
            String nombreUsuario = SesionActual.getInstancia().getUsuarioActual().getNombre();
            String rolUsuario = SesionActual.getInstancia().getUsuarioActual().getRol().getNombre();
            
            vista.mostrarMensaje("Bienvenido, " + nombreUsuario + " (" + rolUsuario + ")", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
            
            vista.dispose(); // Cierra la ventana de login
            
            // TODO: Aquí abriremos el Dashboard Principal más adelante.
            System.out.println("Transición al Menú Principal pendiente...");
            
        } else {
            vista.mostrarMensaje("Credenciales incorrectas o usuario inactivo.", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}