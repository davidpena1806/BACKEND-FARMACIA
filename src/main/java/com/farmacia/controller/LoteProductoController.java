package com.farmacia.controller;

import com.farmacia.entity.LoteProducto;
import com.farmacia.service.LoteProductoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lotes-producto")
public class LoteProductoController extends GenericCrudController<LoteProducto> {

    public LoteProductoController(LoteProductoService service) {
        super(service);
    }
}
