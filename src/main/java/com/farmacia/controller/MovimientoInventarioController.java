package com.farmacia.controller;

import com.farmacia.entity.MovimientoInventario;
import com.farmacia.service.MovimientoInventarioService;
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

/** La bitácora de inventario es de solo lectura, salvo el registro de ajustes manuales (POST). */
@RestController
@RequestMapping("/api/movimientos-inventario")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioService service;

    @GetMapping
    public List<MovimientoInventario> findAll(@RequestParam(required = false) Map<String, String> filters) {
        return service.findAll(filters);
    }

    @GetMapping("/{id}")
    public MovimientoInventario findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<MovimientoInventario> create(@Valid @RequestBody MovimientoInventario movimiento) {
        MovimientoInventario creado = service.create(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.findById(creado.getId()));
    }
}
