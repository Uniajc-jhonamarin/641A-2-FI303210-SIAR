package com.siar.seguridad;
 
import com.siar.modelo.Usuario;
 
public class SesionActual {
    private static SesionActual instancia;
    private Usuario usuarioActual;
 
    // Constructor privado para patrón Singleton
    private SesionActual() {
    }
 
    public static SesionActual getInstancia() {
        if (instancia == null) {
            instancia = new SesionActual();
        }
        return instancia;
    }
 
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }
 
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
    public boolean haySesionActiva() {
        return this.usuarioActual != null;
    }
}