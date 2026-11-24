package com.siar.modelo;

import com.siar.seguridad.SesionActual;
import com.siar.servicio.ComprobanteService;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ModelosYComprobanteTest {

    @Test
    void debeCubrirGettersSettersDeUsuarioRolYFactura() {
        Rol rol = new Rol();
        rol.setId(3);
        rol.setNombre("Cajero");
        rol.setDescripcion("Caja y cobros");
        assertEquals(3, rol.getId());
        assertEquals("Cajero", rol.getNombre());
        assertEquals("Caja y cobros", rol.getDescripcion());
        assertEquals("Cajero", rol.toString());

        LocalDateTime ahora = LocalDateTime.now();
        Usuario u = new Usuario();
        u.setId(5);
        u.setNombre("Juan");
        u.setEmail("juan@siar.com");
        u.setPasswordHash("hash");
        u.setActivo(true);
        u.setRol(rol);
        u.setFechaCreacion(ahora);

        assertEquals(5, u.getId());
        assertEquals("Juan", u.getNombre());
        assertEquals("juan@siar.com", u.getEmail());
        assertEquals("hash", u.getPasswordHash());
        assertTrue(u.isActivo());
        assertEquals(rol, u.getRol());
        assertEquals(ahora, u.getFechaCreacion());

        Factura f = new Factura();
        f.setIdFactura(99);
        f.setIdPedido(12);
        f.setFecha(ahora);
        f.setSubtotal(20000);
        f.setDescuento(1000);

        assertEquals(99, f.getIdFactura());
        assertEquals(12, f.getIdPedido());
        assertEquals(ahora, f.getFecha());
        assertEquals(20000, f.getSubtotal(), 0.01);
        assertEquals(1000, f.getDescuento(), 0.01);
        assertEquals(22800, f.getTotal(), 0.01);
    }

    @Test
    void comprobanteServiceSinDescuentoYGuardadoEnDisco() throws Exception {
        Usuario cajero = new Usuario();
        cajero.setNombre("Cajero <Test> & Co");
        SesionActual.getInstancia().setUsuarioActual(cajero);

        Producto jugo = new Producto();
        jugo.setNombre("Jugo & Limón");
        jugo.setPrecio(10000);

        Pedido pedido = new Pedido();
        pedido.setId(999);
        pedido.setNumeroMesa("7");
        pedido.agregarDetalle(new DetallePedido(jugo, 1)); // < 50.000 -> sin descuento

        ComprobanteService service = new ComprobanteService();
        Factura factura = service.construirFactura(pedido);
        assertEquals(0.0, factura.getDescuento(), 0.01);

        String html = service.generarHtmlComprobante(pedido, factura);
        assertTrue(html.contains("Jugo &amp; Limón"));
        assertTrue(html.contains("Cajero &lt;Test&gt; &amp; Co"));

        File archivo = service.guardarComprobanteHtml(999, html);
        assertTrue(archivo.exists());
        archivo.delete(); // Limpieza después de la prueba
        SesionActual.getInstancia().cerrarSesion();
    }
}