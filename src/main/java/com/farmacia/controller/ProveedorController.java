package com.farmacia.controller;

import com.farmacia.entity.Proveedor;
import com.farmacia.service.ProveedorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController extends GenericCrudController<Proveedor> {

    public ProveedorController(ProveedorService service) {
        super(service);
    }
}
