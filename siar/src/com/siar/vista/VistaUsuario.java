package com.siar.vista;

import com.siar.modelo.Rol;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaUsuario extends JPanel {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private JTextField txtId, txtNombre, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<Rol> cbRol;
    private JCheckBox chkActivo;

    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;

    public VistaUsuario() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PANEL IZQUIERDO: Formulario ---
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        panelFormulario.setPreferredSize(new Dimension(320, 260));

        panelFormulario.add(new JLabel("ID (Automático):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Correo Electrónico:"));
        txtEmail = new JTextField();
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        txtPassword.setToolTipText("Obligatoria al crear. Dejar vacía al editar para mantener la actual.");
        panelFormulario.add(txtPassword);

        panelFormulario.add(new JLabel("Rol:"));
        cbRol = new JComboBox<>();
        panelFormulario.add(cbRol);

        panelFormulario.add(new JLabel("Estado:"));
        chkActivo = new JCheckBox("Usuario Activo", true);
        panelFormulario.add(chkActivo);

        // Botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        btnGuardar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 10));
        panelIzquierdo.add(panelFormulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        add(panelIzquierdo, BorderLayout.WEST);

        // --- PANEL DERECHO: Tabla ---
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Email", "Rol", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Usuarios Registrados"));
        add(scrollTabla, BorderLayout.CENTER);
    }

    public JTable getTablaUsuarios() { return tablaUsuarios; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JComboBox<Rol> getCbRol() { return cbRol; }
    public JCheckBox getChkActivo() { return chkActivo; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }

    public void mostrarMensaje(String msj, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msj, titulo, tipo);
    }
}