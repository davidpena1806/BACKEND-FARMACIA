package com.farmacia.service;

import com.farmacia.entity.DetalleIngreso;
import com.farmacia.entity.Ingreso;
import com.farmacia.entity.LoteProducto;
import com.farmacia.exception.BusinessException;
import com.farmacia.repository.IngresoRepository;
import com.farmacia.repository.LoteProductoRepository;
import com.farmacia.repository.ProveedorRepository;
import com.farmacia.repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class IngresoService extends GenericCrudService<Ingreso> {

    private static final BigDecimal IGV = new BigDecimal("0.18");

    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final LoteProductoRepository loteRepository;
    private final StockService stockService;

    public IngresoService(IngresoRepository repository, ProveedorRepository proveedorRepository,
                          UsuarioRepository usuarioRepository, LoteProductoRepository loteRepository,
                          StockService stockService) {
        super(repository, "Ingreso");
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.loteRepository = loteRepository;
        this.stockService = stockService;
    }

    /** Registra el ingreso (compra) + detalles y SUMA el stock del producto y del lote. */
    @Override
    @Transactional
    public Ingreso create(Ingreso ingreso) {
        if (!proveedorRepository.existsById(ingreso.getProveedorId())) {
            throw new BusinessException("El proveedor indicado no existe.");
        }
        if (!usuarioRepository.existsById(ingreso.getUsuarioId())) {
            throw new BusinessException("El usuario indicado no existe.");
        }

        ingreso.setId(null);
        if (ingreso.getFecha() == null) ingreso.setFecha(LocalDateTime.now());

        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetalleIngreso detalle : ingreso.getDetalles()) {
            LoteProducto lote = loteRepository.findById(detalle.getLoteProductoId())
                    .orElseThrow(() -> new BusinessException("El lote con Id " + detalle.getLoteProductoId() + " no existe."));
            if (!lote.getProductoId().equals(detalle.getProductoId())) {
                throw new BusinessException("El lote '" + lote.getNumeroLote() + "' no pertenece al producto indicado.");
            }
            detalle.setId(null);
            detalle.setIngreso(ingreso);
            detalle.setSubtotal(detalle.getPrecioCompra().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            subtotal = subtotal.add(detalle.getSubtotal());
        }
        BigDecimal impuesto = subtotal.multiply(IGV).setScale(2, RoundingMode.HALF_UP);
        ingreso.setSubtotal(subtotal);
        ingreso.setImpuesto(impuesto);
        ingreso.setTotal(subtotal.add(impuesto));

        Ingreso guardado = repository.save(ingreso);

        for (DetalleIngreso detalle : guardado.getDetalles()) {
            stockService.aumentar(detalle.getProductoId(), detalle.getLoteProductoId(), detalle.getCantidad(),
                    guardado.getUsuarioId(), "INGRESO", "Ingreso #" + guardado.getId());
        }
        return guardado;
    }

    /** Elimina el ingreso y REVIERTE el stock que había sumado (con su movimiento de ajuste). */
    @Override
    @Transactional
    public void delete(Long id) {
        Ingreso ingreso = findById(id);
        for (DetalleIngreso detalle : ingreso.getDetalles()) {
            stockService.disminuir(detalle.getProductoId(), detalle.getLoteProductoId(), detalle.getCantidad(),
                    ingreso.getUsuarioId(), "AJUSTE_ANULACION_INGRESO", "Anulación Ingreso #" + ingreso.getId());
        }
        repository.delete(ingreso);
    }

    @Override
    protected Specification<Ingreso> buildSpecification(Map<String, String> filters) {
        Specification<Ingreso> spec = null;
        Long proveedorId = longParam(filters, "proveedorId");
        LocalDate desde = dateParam(filters, "fechaDesde");
        LocalDate hasta = dateParam(filters, "fechaHasta");
        if (proveedorId != null) spec = and(spec, fieldEquals("proveedorId", proveedorId));
        if (desde != null) {
            spec = and(spec, (root, q, cb) -> cb.greaterThanOrEqualTo(root.<LocalDateTime>get("fecha"), desde.atStartOfDay()));
        }
        if (hasta != null) {
            spec = and(spec, (root, q, cb) -> cb.lessThan(root.<LocalDateTime>get("fecha"), hasta.plusDays(1).atStartOfDay()));
        }
        return spec;
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.DESC, "fecha", "id");
    }
}
