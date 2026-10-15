package com.farmacia.service;

import com.farmacia.entity.MovimientoInventario;
import com.farmacia.repository.MovimientoInventarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
public class MovimientoInventarioService extends GenericCrudService<MovimientoInventario> {

    private static final Set<String> TIPOS_QUE_SUMAN = Set.of("INGRESO", "AJUSTE_POSITIVO");

    private final StockService stockService;

    public MovimientoInventarioService(MovimientoInventarioRepository repository, StockService stockService) {
        super(repository, "Movimiento de inventario");
        this.stockService = stockService;
    }

    /** Ajuste manual de inventario (merma, ajuste positivo/negativo...). Actualiza el stock a través de StockService. */
    @Override
    @Transactional
    public MovimientoInventario create(MovimientoInventario movimiento) {
        String tipo = movimiento.getTipoMovimiento().trim().toUpperCase();
        MovimientoInventario registrado = TIPOS_QUE_SUMAN.contains(tipo)
                ? stockService.aumentar(movimiento.getProductoId(), movimiento.getLoteProductoId(),
                        movimiento.getCantidad(), movimiento.getUsuarioId(), tipo, movimiento.getReferencia())
                : stockService.disminuir(movimiento.getProductoId(), movimiento.getLoteProductoId(),
                        movimiento.getCantidad(), movimiento.getUsuarioId(), tipo, movimiento.getReferencia());
        return registrado;
    }

    /** Filtros opcionales: productoId, tipoMovimiento. */
    @Override
    protected Specification<MovimientoInventario> buildSpecification(Map<String, String> filters) {
        Specification<MovimientoInventario> spec = null;
        Long productoId = longParam(filters, "productoId");
        String tipo = stringParam(filters, "tipoMovimiento");
        if (productoId != null) spec = and(spec, fieldEquals("productoId", productoId));
        if (tipo != null) spec = and(spec, fieldEquals("tipoMovimiento", tipo));
        return spec;
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.DESC, "fecha", "id");
    }
}
