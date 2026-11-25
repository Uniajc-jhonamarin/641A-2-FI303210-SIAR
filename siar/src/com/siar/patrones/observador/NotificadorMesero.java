package com.siar.patrones.observador;

import com.siar.modelo.Pedido;

public class NotificadorMesero implements ObservadorPedido {

    private final Runnable accionRefrescar;

    public NotificadorMesero(Runnable accionRefrescar) {
        this.accionRefrescar = accionRefrescar;
    }

    @Override
    public void actualizar(Pedido pedido) {
        if ("LISTO".equals(pedido.getEstadoActual().getNombre())) {
            System.out.println("[Notificador Mesero] ¡Pedido #" + pedido.getId()
                    + " de Mesa " + pedido.getNumeroMesa() + " está LISTO para entregar!");
        }
        if (accionRefrescar != null) {
            accionRefrescar.run();
        }
    }
}
