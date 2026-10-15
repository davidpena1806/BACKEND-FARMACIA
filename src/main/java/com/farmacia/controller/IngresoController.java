package com.farmacia.controller;

import com.farmacia.entity.DetalleIngreso;
import com.farmacia.entity.Ingreso;
import com.farmacia.service.IngresoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ingresos")
@RequiredArgsConstructor
public class IngresoController {

    private final IngresoService service;

    @GetMapping
    public List<Ingreso> findAll(@RequestParam(required = false) Map<String, String> filters) {
        return service.findAll(filters);
    }

    @GetMapping("/{id}")
    public Ingreso findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/detalles")
    public List<DetalleIngreso> detalles(@PathVariable Long id) {
        return service.findById(id).getDetalles();
    }

    @PostMapping
    public ResponseEntity<Ingreso> create(@Valid @RequestBody Ingreso ingreso) {
        Ingreso creado = service.create(ingreso);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.findById(creado.getId()));
    }

    /** Elimina el ingreso y revierte el stock sumado. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
