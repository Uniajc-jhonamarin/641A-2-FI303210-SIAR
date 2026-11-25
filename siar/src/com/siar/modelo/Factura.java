package com.siar.modelo;

import java.time.LocalDateTime;
import com.siar.patrones.estrategia.*;

public class Factura {

    private int idFactura;
    private int idPedido;
    private LocalDateTime fecha;
    private double subtotal;
    private double descuento;

    private EstrategiaDescuento estrategiaDescuento = new SinDescuento();

    public Factura() {
        this.fecha = LocalDateTime.now();
    }

    public double calcularImpuestos() {
        return subtotal * 0.19;
    }

    public double calcularTotal() {
        return subtotal + calcularImpuestos() - descuento;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getImpuestos() {
        return calcularImpuestos();
    }

    public double getTotal() {
        return calcularTotal();
    }

    public void setEstrategiaDescuento(EstrategiaDescuento estrategia) {
        this.estrategiaDescuento = (estrategia != null) ? estrategia : new SinDescuento();
    }

    public EstrategiaDescuento getEstrategiaDescuento() {
        return estrategiaDescuento;
    }

    public void aplicarDescuento() {
        this.descuento = estrategiaDescuento.calcularDescuento(this.subtotal);
    }
}
