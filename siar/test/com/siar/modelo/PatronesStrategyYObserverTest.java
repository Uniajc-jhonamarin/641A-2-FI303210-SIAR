package com.siar.modelo;

import com.siar.patrones.estrategia.*;
import com.siar.patrones.observador.*;
import com.siar.repositorio.PedidoRepository;
import com.siar.servicio.PedidoService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

public class PatronesStrategyYObserverTest {

    @Test
    void debeAplicarEstrategiasDeDescuentoCorrectamente() {
        Factura f = new Factura();
        f.setSubtotal(100000);

        f.setEstrategiaDescuento(new SinDescuento());
        f.aplicarDescuento();
        assertEquals(0.0, f.getDescuento(), 0.01);
        assertEquals("0%", f.getEstrategiaDescuento().getEtiqueta());

        f.setEstrategiaDescuento(new DescuentoPorcentaje(0.10));
        f.aplicarDescuento();
        assertEquals(10000.0, f.getDescuento(), 0.01);
        assertEquals("10%", f.getEstrategiaDescuento().getEtiqueta());

        f.setEstrategiaDescuento(new DescuentoMontoFijo(15000));
        f.aplicarDescuento();
        assertEquals(15000.0, f.getDescuento(), 0.01);
        assertEquals("Monto fijo", f.getEstrategiaDescuento().getEtiqueta());
    }

    @Test
    void debeNotificarObservadoresAlCrearYAvanzarPedido() {
        PedidoRepository repoFake = new PedidoRepository() {
            @Override public boolean guardarPedido(Pedido p) { p.setId(1); return true; }
            @Override public boolean actualizarEstado(int id, String e) { return true; }
            @Override public List<Pedido> obtenerPedidosActivos() { return Collections.emptyList(); }
            @Override public List<Pedido> obtenerPedidosFacturacion() { return Collections.emptyList(); }
            @Override public List<DetallePedido> obtenerDetalles(int id) { return Collections.emptyList(); }
            @Override public List<Pedido> obtenerTodosLosPedidos() { return Collections.emptyList(); }
        };

        PedidoService service = new PedidoService(repoFake);
        AtomicInteger notificaciones = new AtomicInteger(0);

        ObservadorPedido kds = new PantallaCocinaKDS(notificaciones::incrementAndGet);
        ObservadorPedido mesero = new NotificadorMesero(notificaciones::incrementAndGet);
        service.agregarObservador(kds);
        service.agregarObservador(mesero);

        Producto p = new Producto();
        p.setPrecio(20000);
        Pedido pedido = new Pedido();
        pedido.setNumeroMesa("5");
        pedido.agregarDetalle(new DetallePedido(p, 1));

        assertTrue(service.crearPedido(pedido));        // +2 notificaciones
        assertTrue(service.avanzarEstado(pedido));      // +2 notificaciones (EN_PREPARACION)
        assertTrue(service.avanzarEstado(pedido));      // +2 notificaciones (LISTO)
        assertEquals(6, notificaciones.get());

        service.eliminarObservador(mesero);
        assertTrue(service.avanzarEstado(pedido));      // +1 notificación
        assertEquals(7, notificaciones.get());
    }
}