package com.siar.repositorio;

import com.siar.modelo.Producto;
import java.util.List;

public interface ProductoRepository {

    boolean guardar(Producto producto);

    boolean actualizar(Producto producto);

    boolean eliminar(int id);

    List<Producto> obtenerTodos();
}
