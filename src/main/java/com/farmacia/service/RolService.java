package com.farmacia.service;

import com.farmacia.entity.Rol;
import com.farmacia.repository.RolRepository;
import org.springframework.stereotype.Service;

@Service
public class RolService extends GenericCrudService<Rol> {

    public RolService(RolRepository repository) {
        super(repository, "Rol");
    }
}
