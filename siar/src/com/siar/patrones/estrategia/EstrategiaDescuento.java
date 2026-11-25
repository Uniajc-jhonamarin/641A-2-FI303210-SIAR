package com.siar.patrones.estrategia;

public interface EstrategiaDescuento {

    double calcularDescuento(double subtotal);

    String getEtiqueta();
}
