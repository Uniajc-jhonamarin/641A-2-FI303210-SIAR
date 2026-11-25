package com.siar.patrones.estrategia;

public class SinDescuento implements EstrategiaDescuento {

    @Override
    public double calcularDescuento(double subtotal) {
        return 0.0;
    }

    @Override
    public String getEtiqueta() {
        return "0%";
    }
}
