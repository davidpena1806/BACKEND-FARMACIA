package com.farmacia.controller;

import com.farmacia.entity.BaseEntity;
import com.farmacia.service.GenericCrudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Controlador base con los 5 métodos REST: GET (lista), GET (por id), POST, PUT y DELETE.
 * Los controladores concretos solo indican la ruta (@RequestMapping) y el servicio.
 */
public abstract class GenericCrudController<T extends BaseEntity> {

    protected final GenericCrudService<T> service;

    protected GenericCrudController(GenericCrudService<T> service) {
        this.service = service;
    }

    @GetMapping
    public List<T> findAll(@RequestParam(required = false) Map<String, String> filters) {
        return service.findAll(filters);
    }

    @GetMapping("/{id}")
    public T findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity));
    }

    @PutMapping("/{id}")
    public T update(@PathVariable Long id, @Valid @RequestBody T entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
