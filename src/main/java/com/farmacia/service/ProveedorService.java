package com.farmacia.service;

import com.farmacia.entity.Proveedor;
import com.farmacia.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService extends GenericCrudService<Proveedor> {

    public ProveedorService(ProveedorRepository repository) {
        super(repository, "Proveedor");
    }
}
