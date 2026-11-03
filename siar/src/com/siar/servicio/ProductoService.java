package com.siar.servicio;

import com.siar.modelo.Categoria;
import com.siar.modelo.Producto;
import com.siar.repositorio.CategoriaRepository;
import com.siar.repositorio.ProductoRepository;

import java.util.List;

public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;

    // Inyección de ambas dependencias
    public ProductoService(ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    public List<Categoria> obtenerCategoriasActivas() {
        return categoriaRepo.obtenerTodasActivas();
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepo.obtenerTodos();
    }

    public boolean guardarProducto(Producto p) {
        // Reglas de negocio básicas
        if (p.getPrecio() <= 0) {
            return false;
        }
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            return false;
        }
        if (p.getCategoria() == null) {
            return false;
        }

        return productoRepo.guardar(p);
    }

    public boolean actualizarProducto(Producto p) {
        if (p.getId() <= 0 || p.getPrecio() <= 0) {
            return false;
        }
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            return false;
        }

        return productoRepo.actualizar(p);
    }

    public boolean eliminarProducto(int id) {
        if (id <= 0) {
            return false;
        }
        return productoRepo.eliminar(id);
    }
}
