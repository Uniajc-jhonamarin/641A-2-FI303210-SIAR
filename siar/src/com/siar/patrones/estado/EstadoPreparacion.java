package com.siar.patrones.estado;

import com.siar.modelo.Pedido;

public class EstadoPreparacion implements EstadoPedido {

    @Override
    public void avanzar(Pedido pedido) {
        pedido.setEstadoActual(new EstadoListo());
    }

    @Override
    public String getNombre() {
        return "EN_PREPARACION";
    }
}
