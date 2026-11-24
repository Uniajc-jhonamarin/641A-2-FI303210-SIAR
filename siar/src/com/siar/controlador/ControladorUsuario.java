package com.siar.controlador;

import com.siar.modelo.Rol;
import com.siar.modelo.Usuario;
import com.siar.servicio.UsuarioService;
import com.siar.vista.VistaUsuario;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ControladorUsuario {

    private final VistaUsuario vista;
    private final UsuarioService servicio;
    private List<Usuario> listaActual;

    public ControladorUsuario(VistaUsuario vista, UsuarioService servicio) {
        this.vista = vista;
        this.servicio = servicio;

        cargarRoles();
        cargarTabla();
        inicializarEventos();
    }

    private void cargarRoles() {
        vista.getCbRol().removeAllItems();
        List<Rol> roles = servicio.obtenerRoles();
        for (Rol r : roles) {
            vista.getCbRol().addItem(r);
        }
    }

    private void cargarTabla() {
        vista.getModeloTabla().setRowCount(0);
        listaActual = servicio.obtenerTodosLosUsuarios();

        for (Usuario u : listaActual) {
            vista.getModeloTabla().addRow(new Object[]{
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getRol().getNombre(),
                u.isActivo() ? "Sí" : "No"
            });
        }
    }

    private void inicializarEventos() {
        vista.getBtnGuardar().addActionListener(e -> registrar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

        vista.getTablaUsuarios().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.getTablaUsuarios().getSelectedRow();
                if (fila >= 0) {
                    cargarUsuarioEnFormulario(listaActual.get(fila));
                }
            }
        });
    }

    private void registrar() {
        Usuario u = extraerDatosBasicos();
        String password = new String(vista.getTxtPassword().getPassword());

        if (password.trim().length() < 4) {
            vista.mostrarMensaje("La contraseña debe tener al menos 4 caracteres.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (servicio.registrarUsuario(u, password)) {
            vista.mostrarMensaje("Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        } else {
            vista.mostrarMensaje("Error al registrar. Verifique que el correo no esté duplicado y los campos estén completos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (vista.getTxtId().getText().isEmpty()) return;

        Usuario u = extraerDatosBasicos();
        u.setId(Integer.parseInt(vista.getTxtId().getText()));
        String nuevaPassword = new String(vista.getTxtPassword().getPassword());

        if (servicio.actualizarUsuario(u, nuevaPassword)) {
            vista.mostrarMensaje("Usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTabla();
        } else {
            vista.mostrarMensaje("Error al actualizar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        String idStr = vista.getTxtId().getText();
        if (idStr.isEmpty()) return;

        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Seguro que desea eliminar este usuario?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (servicio.eliminarUsuario(Integer.parseInt(idStr))) {
                vista.mostrarMensaje("Usuario eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTabla();
            } else {
                vista.mostrarMensaje("No se pudo eliminar (no puedes eliminarte a ti mismo o el usuario tiene pedidos asociados).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Usuario extraerDatosBasicos() {
        Usuario u = new Usuario();
        u.setNombre(vista.getTxtNombre().getText().trim());
        u.setEmail(vista.getTxtEmail().getText().trim());
        u.setRol((Rol) vista.getCbRol().getSelectedItem());
        u.setActivo(vista.getChkActivo().isSelected());
        return u;
    }

    private void cargarUsuarioEnFormulario(Usuario u) {
        vista.getTxtId().setText(String.valueOf(u.getId()));
        vista.getTxtNombre().setText(u.getNombre());
        vista.getTxtEmail().setText(u.getEmail());
        vista.getTxtPassword().setText(""); // Nunca mostrar el hash
        vista.getChkActivo().setSelected(u.isActivo());

        for (int i = 0; i < vista.getCbRol().getItemCount(); i++) {
            if (vista.getCbRol().getItemAt(i).getId() == u.getRol().getId()) {
                vista.getCbRol().setSelectedIndex(i);
                break;
            }
        }

        vista.getBtnGuardar().setEnabled(false);
        vista.getBtnActualizar().setEnabled(true);
        vista.getBtnEliminar().setEnabled(true);
    }

    private void limpiarFormulario() {
        vista.getTxtId().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtEmail().setText("");
        vista.getTxtPassword().setText("");
        if (vista.getCbRol().getItemCount() > 0) vista.getCbRol().setSelectedIndex(0);
        vista.getChkActivo().setSelected(true);
        vista.getTablaUsuarios().clearSelection();

        vista.getBtnGuardar().setEnabled(true);
        vista.getBtnActualizar().setEnabled(false);
        vista.getBtnEliminar().setEnabled(false);
    }
}