package com.farmacia.service;

import com.farmacia.entity.Caja;
import com.farmacia.entity.MovimientoCaja;
import com.farmacia.exception.BusinessException;
import com.farmacia.repository.CajaRepository;
import com.farmacia.repository.MovimientoCajaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class MovimientoCajaService extends GenericCrudService<MovimientoCaja> {

    private static final Set<String> TIPOS_VALIDOS = Set.of("INGRESO", "EGRESO");

    private final CajaRepository cajaRepository;

    public MovimientoCajaService(MovimientoCajaRepository repository, CajaRepository cajaRepository) {
        super(repository, "Movimiento de caja");
        this.cajaRepository = cajaRepository;
    }

    /** Solo se pueden registrar movimientos en una caja ABIERTA. */
    @Override
    @Transactional
    public MovimientoCaja create(MovimientoCaja movimiento) {
        Caja caja = cajaRepository.findById(movimiento.getCajaId())
                .orElseThrow(() -> new BusinessException("La caja indicada no existe."));
        if ("CERRADA".equals(caja.getEstado())) {
            throw new BusinessException("No se pueden registrar movimientos en una caja cerrada.");
        }
        String tipo = movimiento.getTipoMovimiento().trim().toUpperCase();
        if (!TIPOS_VALIDOS.contains(tipo)) {
            throw new BusinessException("El tipo de movimiento debe ser INGRESO o EGRESO.");
        }
        movimiento.setId(null);
        movimiento.setTipoMovimiento(tipo);
        movimiento.setFecha(LocalDateTime.now());
        return repository.save(movimiento);
    }

    @Override
    protected Specification<MovimientoCaja> buildSpecification(Map<String, String> filters) {
        Long cajaId = longParam(filters, "cajaId");
        return cajaId == null ? null : fieldEquals("cajaId", cajaId);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.DESC, "fecha");
    }
}
