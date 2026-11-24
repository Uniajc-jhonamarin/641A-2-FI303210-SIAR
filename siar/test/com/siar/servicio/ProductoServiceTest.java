package com.siar.servicio;

import com.siar.modelo.Categoria;
import com.siar.modelo.Producto;
import com.siar.repositorio.CategoriaRepository;
import com.siar.repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoServiceTest {

    private ProductoService productoService;
    private List<Producto> bdMemoria;

    @BeforeEach
    void setUp() {
        bdMemoria = new ArrayList<>();

        ProductoRepository prodRepoFake = new ProductoRepository() {
            @Override public boolean guardar(Producto p) { p.setId(bdMemoria.size() + 1); return bdMemoria.add(p); }
            @Override public boolean actualizar(Producto p) { return bdMemoria.stream().anyMatch(x -> x.getId() == p.getId()); }
            @Override public boolean eliminar(int id) { return bdMemoria.removeIf(x -> x.getId() == id); }
            @Override public List<Producto> obtenerTodos() { return bdMemoria; }
        };

        CategoriaRepository catRepoFake = () -> List.of(new Categoria(1, "Platos Fuertes", "Principales", true));

        productoService = new ProductoService(prodRepoFake, catRepoFake);
    }

    @Test
    void debeGuardarProductoValido() {
        Producto p = new Producto();
        p.setNombre("Arroz chino");
        p.setPrecio(30000);
        p.setCategoria(new Categoria(1, "Platos Fuertes", "", true));

        assertTrue(productoService.guardarProducto(p));
        assertEquals(1, productoService.obtenerTodosLosProductos().size());
    }

    @Test
    void noDebeGuardarProductoConPrecioCeroONegativo() {
        Producto p = new Producto();
        p.setNombre("Error");
        p.setPrecio(-500);
        p.setCategoria(new Categoria(1, "Platos Fuertes", "", true));

        assertFalse(productoService.guardarProducto(p));
    }

    @Test
    void noDebeGuardarProductoSinNombreOSinCategoria() {
        Producto p = new Producto();
        p.setNombre("   ");
        p.setPrecio(10000);
        assertFalse(productoService.guardarProducto(p));
    }

    @Test
    void debeEliminarProductoExistenteYRechazarIdInvalido() {
        Producto p = new Producto();
        p.setNombre("Limonada");
        p.setPrecio(5000);
        p.setCategoria(new Categoria(1, "Bebidas", "", true));
        productoService.guardarProducto(p);

        assertFalse(productoService.eliminarProducto(0));
        assertTrue(productoService.eliminarProducto(p.getId()));
    }
}