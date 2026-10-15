package com.farmacia.service;

import com.farmacia.entity.DetalleVenta;
import com.farmacia.entity.Venta;
import com.farmacia.exception.BusinessException;
import com.farmacia.repository.ClienteRepository;
import com.farmacia.repository.UsuarioRepository;
import com.farmacia.repository.VentaRepository;
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
public class VentaService extends GenericCrudService<Venta> {

    private static final BigDecimal IGV = new BigDecimal("0.18");

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final StockService stockService;

    public VentaService(VentaRepository repository, ClienteRepository clienteRepository,
                        UsuarioRepository usuarioRepository, StockService stockService) {
        super(repository, "Venta");
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.stockService = stockService;
    }

    /** Registra la venta + detalles y descuenta el stock. Si falta stock, se revierte todo (transacción). */
    @Override
    @Transactional
    public Venta create(Venta venta) {
        if (!clienteRepository.existsById(venta.getClienteId())) {
            throw new BusinessException("El cliente indicado no existe.");
        }
        if (!usuarioRepository.existsById(venta.getUsuarioId())) {
            throw new BusinessException("El usuario indicado no existe.");
        }

        venta.setId(null);
        venta.setEstado("EMITIDA");
        if (venta.getFecha() == null) venta.setFecha(LocalDateTime.now());

        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetalleVenta detalle : venta.getDetalles()) {
            detalle.setId(null);
            detalle.setVenta(venta);
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            subtotal = subtotal.add(detalle.getSubtotal());
        }
        BigDecimal impuesto = subtotal.multiply(IGV).setScale(2, RoundingMode.HALF_UP);
        venta.setSubtotal(subtotal);
        venta.setImpuesto(impuesto);
        venta.setTotal(subtotal.add(impuesto));

        Venta guardada = repository.save(venta);

        for (DetalleVenta detalle : guardada.getDetalles()) {
            stockService.disminuir(detalle.getProductoId(), null, detalle.getCantidad(),
                    guardada.getUsuarioId(), "VENTA", "Venta #" + guardada.getId());
        }
        return guardada;
    }

    /** Anula la venta y repone el stock. */
    @Transactional
    public Venta anular(Long id) {
        Venta venta = findById(id);
        if ("ANULADA".equals(venta.getEstado())) {
            throw new BusinessException("La venta ya se encuentra anulada.");
        }
        venta.setEstado("ANULADA");
        for (DetalleVenta detalle : venta.getDetalles()) {
            stockService.aumentar(detalle.getProductoId(), null, detalle.getCantidad(),
                    venta.getUsuarioId(), "AJUSTE_ANULACION", "Anulación Venta #" + venta.getId());
        }
        return repository.save(venta);
    }

    /** Filtros opcionales: clienteId, estado, fechaDesde, fechaHasta (yyyy-MM-dd). */
    @Override
    protected Specification<Venta> buildSpecification(Map<String, String> filters) {
        Specification<Venta> spec = null;
        Long clienteId = longParam(filters, "clienteId");
        String estado = stringParam(filters, "estado");
        LocalDate desde = dateParam(filters, "fechaDesde");
        LocalDate hasta = dateParam(filters, "fechaHasta");
        if (clienteId != null) spec = and(spec, fieldEquals("clienteId", clienteId));
        if (estado != null) spec = and(spec, fieldEquals("estado", estado));
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
