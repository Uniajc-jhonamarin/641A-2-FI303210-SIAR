package com.siar.modelo;

import com.siar.patrones.estado.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoYEstadosTest {

    @Test
    void pedidoDebeIniciarEnEstadoPendienteYRecorrerCicloCompleto() {
        Pedido pedido = new Pedido();
        assertEquals("PENDIENTE", pedido.getEstadoActual().getNombre());

        pedido.procesarSiguienteEstado();
        assertEquals("EN_PREPARACION", pedido.getEstadoActual().getNombre());
        assertInstanceOf(EstadoPreparacion.class, pedido.getEstadoActual());

        pedido.procesarSiguienteEstado();
        assertEquals("LISTO", pedido.getEstadoActual().getNombre());
        assertInstanceOf(EstadoListo.class, pedido.getEstadoActual());

        pedido.procesarSiguienteEstado();
        assertEquals("ENTREGADO", pedido.getEstadoActual().getNombre());
        assertInstanceOf(EstadoEntregado.class, pedido.getEstadoActual());

        pedido.procesarSiguienteEstado();
        assertEquals("FACTURADO", pedido.getEstadoActual().getNombre());
        assertInstanceOf(EstadoFacturado.class, pedido.getEstadoActual());

        // Estado final: no debe cambiar al avanzar de nuevo
        pedido.procesarSiguienteEstado();
        assertEquals("FACTURADO", pedido.getEstadoActual().getNombre());
    }

    @Test
    void debeCalcularSubtotalesYTotalCorrectamente() {
        Producto arroz = new Producto();
        arroz.setId(1);
        arroz.setNombre("Arroz chino");
        arroz.setPrecio(30000.0);

        Producto jugo = new Producto();
        jugo.setId(2);
        jugo.setNombre("Jugo natural");
        jugo.setPrecio(8000.0);

        Pedido pedido = new Pedido();
        pedido.agregarDetalle(new DetallePedido(arroz, 2)); // 60.000
        pedido.agregarDetalle(new DetallePedido(jugo, 1));  // 8.000

        assertEquals(2, pedido.getDetalles().size());
        assertEquals(68000.0, pedido.getTotal(), 0.01);
    }

    @Test
    void actualizarCantidadEnDetalleDebeRecalcularSubtotal() {
        Producto p = new Producto();
        p.setPrecio(15000.0);

        DetallePedido dp = new DetallePedido(p, 1);
        assertEquals(15000.0, dp.getSubtotal(), 0.01);

        dp.setCantidad(3);
        assertEquals(45000.0, dp.getSubtotal(), 0.01);
    }
}