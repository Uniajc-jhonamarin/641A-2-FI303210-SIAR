package com.siar.patrones.observador;

import com.siar.modelo.Pedido;

public class PantallaCocinaKDS implements ObservadorPedido {

    private final Runnable accionRefrescar;

    public PantallaCocinaKDS(Runnable accionRefrescar) {
        this.accionRefrescar = accionRefrescar;
    }

    @Override
    public void actualizar(Pedido pedido) {
        System.out.println("[KDS Cocina] Pedido #" + pedido.getId()
                + " (Mesa " + pedido.getNumeroMesa() + ") -> "
                + pedido.getEstadoActual().getNombre());
        if (accionRefrescar != null) {
            accionRefrescar.run();
        }
    }
}
