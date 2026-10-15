package com.farmacia.service;

import com.farmacia.entity.Producto;
import com.farmacia.repository.ProductoRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductoService extends GenericCrudService<Producto> {

    public ProductoService(ProductoRepository repository) {
        super(repository, "Producto");
    }

    /** Filtros opcionales: ?categoriaId=1&activo=true */
    @Override
    protected Specification<Producto> buildSpecification(Map<String, String> filters) {
        Specification<Producto> spec = null;
        Long categoriaId = longParam(filters, "categoriaId");
        Boolean activo = boolParam(filters, "activo");
        if (categoriaId != null) spec = and(spec, fieldEquals("categoriaId", categoriaId));
        if (activo != null) spec = and(spec, fieldEquals("activo", activo));
        return spec;
    }
}
