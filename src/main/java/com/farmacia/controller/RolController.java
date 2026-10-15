package com.farmacia.controller;

import com.farmacia.entity.Rol;
import com.farmacia.service.RolService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RolController extends GenericCrudController<Rol> {

    public RolController(RolService service) {
        super(service);
    }
}
