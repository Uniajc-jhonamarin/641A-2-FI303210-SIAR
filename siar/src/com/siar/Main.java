package com.siar;

import com.siar.controlador.ControladorLogin;
import com.siar.persistencia.UsuarioRepositoryImpl;
import com.siar.repositorio.UsuarioRepository;
import com.siar.servicio.AuthService;
import com.siar.vista.VistaLogin;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        
        // Ejecutar la interfaz gráfica en el hilo especial de eventos de Swing (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Instanciar el acceso a datos y lógica de negocio
                UsuarioRepository repoUsuario = new UsuarioRepositoryImpl();
                AuthService authService = new AuthService(repoUsuario);

                // 2. Instanciar la vista gráfica
                VistaLogin vistaLogin = new VistaLogin();

                // 3. Instanciar el controlador, pasando vista y servicio
                ControladorLogin controladorLogin = new ControladorLogin(vistaLogin, authService);

                // 4. Iniciar la aplicación mostrando la ventana
                controladorLogin.iniciar();
            }
        });
    }
}