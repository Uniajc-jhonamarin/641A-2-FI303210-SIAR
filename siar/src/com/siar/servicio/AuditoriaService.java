package com.siar.servicio;

import com.siar.modelo.Usuario;
import com.siar.persistencia.AuditoriaRepositoryImpl;
import com.siar.seguridad.SesionActual;

public class AuditoriaService {

    private final AuditoriaRepositoryImpl repo = new AuditoriaRepositoryImpl();

    public void registrarEvento(String accion, String detalle) {
        Usuario actual = SesionActual.getInstancia().getUsuarioActual();
        String email = (actual != null) ? actual.getEmail() : "SISTEMA/ANONIMO";
        repo.registrar(email, accion, detalle);
    }
}