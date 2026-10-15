package com.farmacia.service;

import com.farmacia.entity.Categoria;
import com.farmacia.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService extends GenericCrudService<Categoria> {

    public CategoriaService(CategoriaRepository repository) {
        super(repository, "Categoría");
    }
}
