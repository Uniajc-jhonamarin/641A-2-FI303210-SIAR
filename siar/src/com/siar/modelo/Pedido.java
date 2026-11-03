package com.siar.modelo;

import com.siar.patrones.estado.EstadoPedido;
import com.siar.patrones.estado.EstadoPendiente;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private int id;
    private String numeroMesa;
    private String nombreCliente;
    private List<DetallePedido> detalles;
    private double total;
    private LocalDateTime fechaCreacion;

    // Motor del Patrón State
    private EstadoPedido estadoActual;

    public Pedido() {
        this.detalles = new ArrayList<>();
        this.estadoActual = new EstadoPendiente(); // Todo pedido nace Pendiente
        this.total = 0.0;
    }

    // Método principal del Patrón State
    public void procesarSiguienteEstado() {
        this.estadoActual.avanzar(this);
    }

    public void agregarDetalle(DetallePedido detalle) {
        this.detalles.add(detalle);
        calcularTotal();
    }

    public void calcularTotal() {
        this.total = 0.0;
        for (DetallePedido dp : detalles) {
            this.total += dp.getSubtotal();
        }
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(String numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoPedido getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoPedido estadoActual) {
        this.estadoActual = estadoActual;
    }
}
