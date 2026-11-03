package com.siar.repositorio;

import com.siar.modelo.Categoria;
import java.util.List;

public interface CategoriaRepository {

    List<Categoria> obtenerTodasActivas();
}
