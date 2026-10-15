package com.farmacia.controller;

import com.farmacia.entity.Categoria;
import com.farmacia.service.CategoriaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController extends GenericCrudController<Categoria> {

    public CategoriaController(CategoriaService service) {
        super(service);
    }
}
