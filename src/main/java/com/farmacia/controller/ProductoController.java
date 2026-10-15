package com.farmacia.controller;

import com.farmacia.entity.LoteProducto;
import com.farmacia.entity.Producto;
import com.farmacia.service.LoteProductoService;
import com.farmacia.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController extends GenericCrudController<Producto> {

    private final LoteProductoService loteService;

    public ProductoController(ProductoService service, LoteProductoService loteService) {
        super(service);
        this.loteService = loteService;
    }

    /** Lotes registrados de un producto. */
    @GetMapping("/{id}/lotes")
    public List<LoteProducto> lotes(@PathVariable Long id) {
        service.findById(id);
        return loteService.findAll(Map.of("productoId", id.toString()));
    }
}
