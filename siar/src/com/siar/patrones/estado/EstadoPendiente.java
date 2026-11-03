package com.siar.patrones.estado;

import com.siar.modelo.Pedido;

public class EstadoPendiente implements EstadoPedido {
    
    @Override
    public void avanzar(Pedido pedido) {
        pedido.setEstadoActual(new EstadoPreparacion()); 
    }

    @Override
    public String getNombre() {
        return "PENDIENTE";
    }
}