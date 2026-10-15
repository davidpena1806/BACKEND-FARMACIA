package com.farmacia.controller;

import com.farmacia.entity.Cliente;
import com.farmacia.entity.Venta;
import com.farmacia.service.ClienteService;
import com.farmacia.service.VentaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends GenericCrudController<Cliente> {

    private final VentaService ventaService;

    public ClienteController(ClienteService service, VentaService ventaService) {
        super(service);
        this.ventaService = ventaService;
    }

    /** Historial de ventas de un cliente. */
    @GetMapping("/{id}/ventas")
    public List<Venta> ventas(@PathVariable Long id) {
        service.findById(id);
        return ventaService.findAll(Map.of("clienteId", id.toString()));
    }
}
