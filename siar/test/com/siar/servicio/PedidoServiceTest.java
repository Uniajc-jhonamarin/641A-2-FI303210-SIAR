package com.siar.servicio;

import com.siar.modelo.DetallePedido;
import com.siar.modelo.Pedido;
import com.siar.modelo.Producto;
import com.siar.repositorio.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoServiceTest {

    private PedidoService pedidoService;
    private String ultimoEstadoPersistido;

    @BeforeEach
    void setUp() {
        ultimoEstadoPersistido = null;

        PedidoRepository repoFake = new PedidoRepository() {
            @Override public boolean guardarPedido(Pedido pedido) { pedido.setId(1); return true; }
            @Override public boolean actualizarEstado(int idPedido, String nuevoEstado) {
                ultimoEstadoPersistido = nuevoEstado;
                return true;
            }
            @Override public List<Pedido> obtenerPedidosActivos() { return new ArrayList<>(); }
            @Override public List<Pedido> obtenerPedidosFacturacion() { return new ArrayList<>(); }
            @Override public List<DetallePedido> obtenerDetalles(int idPedido) { return new ArrayList<>(); }
            @Override public List<Pedido> obtenerTodosLosPedidos() { return new ArrayList<>(); }
        };

        pedidoService = new PedidoService(repoFake);
    }

    @Test
    void noDebeCrearPedidoSinProductosOSinMesa() {
        Pedido vacio = new Pedido();
        vacio.setNumeroMesa("1");
        assertFalse(pedidoService.crearPedido(vacio));

        Producto prod = new Producto();
        prod.setPrecio(10000);
        Pedido sinMesa = new Pedido();
        sinMesa.agregarDetalle(new DetallePedido(prod, 1));
        assertFalse(pedidoService.crearPedido(sinMesa));
    }

    @Test
    void debeCrearPedidoValidoYAvanzarEstado() {
        Producto prod = new Producto();
        prod.setPrecio(20000);

        Pedido pedido = new Pedido();
        pedido.setNumeroMesa("2");
        pedido.agregarDetalle(new DetallePedido(prod, 2));

        assertTrue(pedidoService.crearPedido(pedido));
        assertEquals(40000.0, pedido.getTotal(), 0.01);

        assertTrue(pedidoService.avanzarEstado(pedido));
        assertEquals("EN_PREPARACION", ultimoEstadoPersistido);
    }
}