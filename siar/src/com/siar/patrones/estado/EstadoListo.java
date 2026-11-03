package com.siar.patrones.estado;

import com.siar.modelo.Pedido;

public class EstadoListo implements EstadoPedido {

    @Override
    public void avanzar(Pedido pedido) {
        pedido.setEstadoActual(new EstadoEntregado());
    }

    @Override
    public String getNombre() {
        return "LISTO";
    }
}
