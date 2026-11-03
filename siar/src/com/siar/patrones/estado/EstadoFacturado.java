package com.siar.patrones.estado;

import com.siar.modelo.Pedido;

public class EstadoFacturado implements EstadoPedido {

    @Override
    public void avanzar(Pedido pedido) {
        // Es el estado final, ciclo cerrado.
        System.out.println("El pedido ya está facturado. No avanza más.");
    }

    @Override
    public String getNombre() {
        return "FACTURADO";
    }
}
