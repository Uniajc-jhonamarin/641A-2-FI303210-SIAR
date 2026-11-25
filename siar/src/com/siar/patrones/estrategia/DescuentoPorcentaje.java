package com.siar.patrones.estrategia;

public class DescuentoPorcentaje implements EstrategiaDescuento {

    private final double porcentaje; // Ej: 0.10 para 10%

    public DescuentoPorcentaje(double porcentaje) {
        this.porcentaje = Math.max(0.0, Math.min(porcentaje, 1.0));
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * porcentaje;
    }

    @Override
    public String getEtiqueta() {
        return Math.round(porcentaje * 100) + "%";
    }
}
