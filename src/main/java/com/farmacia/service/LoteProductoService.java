package com.farmacia.service;

import com.farmacia.entity.LoteProducto;
import com.farmacia.repository.LoteProductoRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoteProductoService extends GenericCrudService<LoteProducto> {

    public LoteProductoService(LoteProductoRepository repository) {
        super(repository, "Lote");
    }

    /** Filtro opcional: ?productoId=1 */
    @Override
    protected Specification<LoteProducto> buildSpecification(Map<String, String> filters) {
        Long productoId = longParam(filters, "productoId");
        return productoId == null ? null : fieldEquals("productoId", productoId);
    }
}
