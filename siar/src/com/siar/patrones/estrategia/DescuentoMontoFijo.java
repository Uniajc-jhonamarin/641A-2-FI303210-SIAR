package com.siar.patrones.estrategia;

public class DescuentoMontoFijo implements EstrategiaDescuento {

    private final double monto;

    public DescuentoMontoFijo(double monto) {
        this.monto = Math.max(0.0, monto);
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return Math.min(monto, subtotal);
    }

    @Override
    public String getEtiqueta() {
        return "Monto fijo";
    }
}
