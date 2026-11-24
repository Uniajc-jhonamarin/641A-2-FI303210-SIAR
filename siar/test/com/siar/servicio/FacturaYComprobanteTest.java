package com.siar.servicio;

import com.siar.modelo.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FacturaYComprobanteTest {

    @Test
    void facturaDebeCalcularIvaYTotalSinDescuento() {
        Factura f = new Factura();
        f.setIdPedido(3);
        f.setSubtotal(30000.0);
        f.setDescuento(0.0);

        assertEquals(5700.0, f.getImpuestos(), 0.01);
        assertEquals(35700.0, f.getTotal(), 0.01);
    }

    @Test
    void comprobanteServiceDebeAplicarDescuentoSiSubtotalEsMayorOIgualA50000() {
        ComprobanteService service = new ComprobanteService();

        Producto plato = new Producto();
        plato.setNombre("Bandeja Paisa");
        plato.setPrecio(30000.0);

        Pedido pedido = new Pedido();
        pedido.setId(10);
        pedido.setNumeroMesa("4");
        pedido.agregarDetalle(new DetallePedido(plato, 2)); // Subtotal 60.000

        Factura factura = service.construirFactura(pedido);

        assertEquals(60000.0, factura.getSubtotal(), 0.01);
        assertEquals(6000.0, factura.getDescuento(), 0.01);   // 10%
        assertEquals(11400.0, factura.getImpuestos(), 0.01); // 19%
        assertEquals(65400.0, factura.getTotal(), 0.01);

        String html = service.generarHtmlComprobante(pedido, factura);
        assertTrue(html.contains("Bandeja Paisa"));
        assertTrue(html.contains("Mesa 4"));
        assertTrue(html.contains("10%"));
    }
}