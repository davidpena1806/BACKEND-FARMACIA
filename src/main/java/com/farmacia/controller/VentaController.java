package com.farmacia.controller;

import com.farmacia.entity.DetalleVenta;
import com.farmacia.entity.Venta;
import com.farmacia.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/** Las ventas no se editan ni se borran: se registran y, si es necesario, se anulan (repone stock). */
@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService service;

    @GetMapping
    public List<Venta> findAll(@RequestParam(required = false) Map<String, String> filters) {
        return service.findAll(filters);
    }

    @GetMapping("/{id}")
    public Venta findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/detalles")
    public List<DetalleVenta> detalles(@PathVariable Long id) {
        return service.findById(id).getDetalles();
    }

    @PostMapping
    public ResponseEntity<Venta> create(@Valid @RequestBody Venta venta) {
        Venta creada = service.create(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.findById(creada.getId()));
    }

    @PostMapping("/{id}/anular")
    public Venta anular(@PathVariable Long id) {
        service.anular(id);
        return service.findById(id);
    }
}
