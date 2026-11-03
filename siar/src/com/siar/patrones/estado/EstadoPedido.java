package com.siar.patrones.estado;

import com.siar.modelo.Pedido;

public interface EstadoPedido {

    // Avanza el pedido al siguiente estado lógico
    void avanzar(Pedido pedido);

    // Retorna el nombre del estado para guardarlo en la BD o mostrarlo en la UI
    String getNombre();
}
