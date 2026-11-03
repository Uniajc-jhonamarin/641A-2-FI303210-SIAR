package com.siar.patrones.estado;

import com.siar.modelo.Pedido;

public class EstadoEntregado implements EstadoPedido {

    @Override
    public void avanzar(Pedido pedido) {
        pedido.setEstadoActual(new EstadoFacturado()); 
    }

    @Override
    public String getNombre() {
        return "ENTREGADO";
    }
}
