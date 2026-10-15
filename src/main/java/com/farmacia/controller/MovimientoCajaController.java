package com.farmacia.controller;

import com.farmacia.entity.MovimientoCaja;
import com.farmacia.service.MovimientoCajaService;
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

@RestController
@RequestMapping("/api/movimientos-caja")
@RequiredArgsConstructor
public class MovimientoCajaController {

    private final MovimientoCajaService service;

    @GetMapping
    public List<MovimientoCaja> findAll(@RequestParam(required = false) Map<String, String> filters) {
        return service.findAll(filters);
    }

    @GetMapping("/{id}")
    public MovimientoCaja findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<MovimientoCaja> create(@Valid @RequestBody MovimientoCaja movimiento) {
        MovimientoCaja creado = service.create(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.findById(creado.getId()));
    }
}
