package com.farmacia.service;

import com.farmacia.entity.Cliente;
import com.farmacia.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends GenericCrudService<Cliente> {

    public ClienteService(ClienteRepository repository) {
        super(repository, "Cliente");
    }
}
