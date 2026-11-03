package com.siar.vista;

import javax.swing.*;
import java.awt.*;

public class VistaDashboard extends JFrame {

    private JMenuItem menuUsuarios;
    private JMenuItem menuProductos;
    private JMenuItem menuPedidos;
    private JMenuItem menuFacturacion;
    private JMenuItem menuCerrarSesion;
    private JPanel panelContenido; // Contenedor dinámico para los módulos
    private JMenuItem menuCocina;

    public VistaDashboard() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("SIAR - Sistema Integral de Administración para Restaurantes");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra de Menú Superior
        JMenuBar barraMenu = new JMenuBar();

        // Menú Sistema
        JMenu menuSistema = new JMenu("Sistema");
        menuCerrarSesion = new JMenuItem("Cerrar Sesión");
        menuSistema.add(menuCerrarSesion);

        // Menú Administración
        JMenu menuAdmin = new JMenu("Administración");
        menuUsuarios = new JMenuItem("Gestión de Usuarios");
        menuProductos = new JMenuItem("Gestión de Menú (Productos)");
        menuAdmin.add(menuUsuarios);
        menuAdmin.add(menuProductos);

        // Menú Operación
        JMenu menuOperacion = new JMenu("Operación");
        menuPedidos = new JMenuItem("Gestión de Pedidos");
        menuCocina = new JMenuItem("Monitor de Cocina (KDS)");
        menuFacturacion = new JMenuItem("Facturación y Caja");
        menuOperacion.add(menuPedidos);
        menuOperacion.add(menuCocina);
        menuOperacion.add(menuFacturacion);

        barraMenu.add(menuSistema);
        barraMenu.add(menuAdmin);
        barraMenu.add(menuOperacion);
        setJMenuBar(barraMenu);

        // Panel de Contenido Central (CardLayout para cambiar de pantallas)
        panelContenido = new JPanel(new CardLayout());

        // Pantalla de bienvenida por defecto
        JLabel lblBienvenida = new JLabel("Seleccione un módulo en el menú superior", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));
        lblBienvenida.setForeground(Color.GRAY);
        panelContenido.add(lblBienvenida, "Bienvenida");

        add(panelContenido, BorderLayout.CENTER);
    }

    public JMenuItem getMenuUsuarios() {
        return menuUsuarios;
    }

    public JMenuItem getMenuProductos() {
        return menuProductos;
    }

    public JMenuItem getMenuPedidos() {
        return menuPedidos;
    }

    public JMenuItem getMenuFacturacion() {
        return menuFacturacion;
    }

    public JMenuItem getMenuCerrarSesion() {
        return menuCerrarSesion;
    }

    public JPanel getPanelContenido() {
        return panelContenido;
    }

    public JMenuItem getMenuCocina() {
        return menuCocina;
    }
}
