package com.farmacia.controller;

import com.farmacia.dto.CajaAperturaRequest;
import com.farmacia.dto.CajaCierreRequest;
import com.farmacia.entity.Caja;
import com.farmacia.service.CajaService;
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
@RequestMapping("/api/cajas")
@RequiredArgsConstructor
public class CajaController {

    private final CajaService service;

    @GetMapping
    public List<Caja> findAll(@RequestParam(required = false) Map<String, String> filters) {
        return service.findAll(filters);
    }

    @GetMapping("/{id}")
    public Caja findById(@PathVariable Long id) {
        return service.findById(id);
    }

    /** Apertura de caja. */
    @PostMapping
    public ResponseEntity<Caja> aperturar(@Valid @RequestBody CajaAperturaRequest request) {
        Caja caja = service.aperturar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.findById(caja.getId()));
    }

    /** Cierre de caja con el monto final contado. */
    @PostMapping("/{id}/cierre")
    public Caja cerrar(@PathVariable Long id, @Valid @RequestBody CajaCierreRequest request) {
        service.cerrar(id, request);
        return service.findById(id);
    }
}
