package com.siar.controlador;

import com.siar.seguridad.SesionActual;
import com.siar.vista.VistaDashboard;
import com.siar.vista.VistaLogin;
import com.siar.vista.VistaPedido;
import com.siar.vista.VistaProducto;
import com.siar.vista.VistaCocina;
import com.siar.vista.VistaFacturacion;

import com.siar.servicio.AuthService;
import com.siar.servicio.PedidoService;
import com.siar.servicio.ProductoService;

import com.siar.persistencia.UsuarioRepositoryImpl;
import com.siar.persistencia.CategoriaRepositoryImpl;
import com.siar.persistencia.ProductoRepositoryImpl;
import com.siar.persistencia.PedidoRepositoryImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.awt.CardLayout;

public class ControladorDashboard {

    private final VistaDashboard vista;

    public ControladorDashboard(VistaDashboard vista) {
        this.vista = vista;
        aplicarPermisosPorRol();
        inicializarEventos();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    private void aplicarPermisosPorRol() {
        String rol = SesionActual.getInstancia().getUsuarioActual().getRol().getNombre();
        vista.setTitle("SIAR - Usuario: " + SesionActual.getInstancia().getUsuarioActual().getNombre() + " | Rol: " + rol);

        // Ocultar módulos según el rol (RF05 y RNF05)
        if (rol.equals("Mesero")) {
            vista.getMenuUsuarios().setVisible(false);
            vista.getMenuProductos().setVisible(false);
            vista.getMenuFacturacion().setVisible(false);
        } else if (rol.equals("Cocinero")) {
            vista.getMenuUsuarios().setVisible(false);
            vista.getMenuProductos().setVisible(false);
            vista.getMenuFacturacion().setVisible(false);
            // El cocinero solo ve la pantalla de KDS (Pedidos)
        } else if (rol.equals("Cajero")) {
            vista.getMenuUsuarios().setVisible(false);
            vista.getMenuProductos().setVisible(false);
        }
        // El Administrador ve todo, no se oculta nada.
    }

    private void inicializarEventos() {
        vista.getMenuCerrarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });

        vista.getMenuProductos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirModuloProductos();
            }
        });

        vista.getMenuPedidos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirModuloPedidos();
            }
        });

        vista.getMenuCocina().addActionListener(e -> abrirModuloCocina());
        vista.getMenuFacturacion().addActionListener(e -> abrirModuloFacturacion());
    }

    private void cerrarSesion() {
        SesionActual.getInstancia().cerrarSesion();
        vista.dispose();

        // Reiniciar el Login
        VistaLogin vistaLogin = new VistaLogin();
        AuthService authService = new AuthService(new UsuarioRepositoryImpl());
        ControladorLogin ctrlLogin = new ControladorLogin(vistaLogin, authService);
        ctrlLogin.iniciar();
    }

    private void abrirModuloProductos() {
        // 1. Instanciar dependencias del módulo
        ProductoService productoService = new ProductoService(new ProductoRepositoryImpl(), new CategoriaRepositoryImpl());

        // 2. Crear la vista y su controlador
        VistaProducto vistaProducto = new VistaProducto();
        new ControladorProducto(vistaProducto, productoService);

        // 3. Añadir el panel al Dashboard y mostrarlo
        vista.getPanelContenido().add(vistaProducto, "ModuloProductos");
        CardLayout cl = (CardLayout) vista.getPanelContenido().getLayout();
        cl.show(vista.getPanelContenido(), "ModuloProductos");
    }

    private void abrirModuloPedidos() {
        // 1. Instanciar dependencias
        PedidoService pedidoService = new PedidoService(new PedidoRepositoryImpl());
        ProductoService productoService = new ProductoService(new ProductoRepositoryImpl(), new CategoriaRepositoryImpl());

        // 2. Crear Vista y Controlador
        VistaPedido vistaPedido = new VistaPedido();
        new ControladorPedido(vistaPedido, pedidoService, productoService);

        // 3. Añadir al Dashboard
        vista.getPanelContenido().add(vistaPedido, "ModuloPedidos");
        CardLayout cl = (CardLayout) vista.getPanelContenido().getLayout();
        cl.show(vista.getPanelContenido(), "ModuloPedidos");
    }

    private void abrirModuloCocina() {
        VistaCocina vistaCocina = new VistaCocina();
        new ControladorCocina(vistaCocina, new PedidoService(new PedidoRepositoryImpl()));

        vista.getPanelContenido().add(vistaCocina, "ModuloCocina");
        java.awt.CardLayout cl = (java.awt.CardLayout) vista.getPanelContenido().getLayout();
        cl.show(vista.getPanelContenido(), "ModuloCocina");
    }

    private void abrirModuloFacturacion() {
        VistaFacturacion vistaFacturacion = new VistaFacturacion();
        new ControladorFacturacion(vistaFacturacion, new PedidoService(new PedidoRepositoryImpl()));

        vista.getPanelContenido().add(vistaFacturacion, "ModuloFacturacion");
        java.awt.CardLayout cl = (java.awt.CardLayout) vista.getPanelContenido().getLayout();
        cl.show(vista.getPanelContenido(), "ModuloFacturacion");
    }

}
