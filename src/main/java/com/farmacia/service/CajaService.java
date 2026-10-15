package com.farmacia.service;

import com.farmacia.dto.CajaAperturaRequest;
import com.farmacia.dto.CajaCierreRequest;
import com.farmacia.entity.Caja;
import com.farmacia.exception.BusinessException;
import com.farmacia.repository.CajaRepository;
import com.farmacia.repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CajaService extends GenericCrudService<Caja> {

    private final CajaRepository cajaRepository;
    private final UsuarioRepository usuarioRepository;

    public CajaService(CajaRepository repository, UsuarioRepository usuarioRepository) {
        super(repository, "Caja");
        this.cajaRepository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    /** Un usuario no puede tener dos cajas abiertas a la vez. */
    @Transactional
    public Caja aperturar(CajaAperturaRequest request) {
        if (!usuarioRepository.existsById(request.usuarioId())) {
            throw new BusinessException("El usuario indicado no existe.");
        }
        if (cajaRepository.existsByUsuarioIdAndEstado(request.usuarioId(), "ABIERTA")) {
            throw new BusinessException("El usuario ya tiene una caja abierta. Debe cerrarla antes de abrir otra.");
        }
        Caja caja = new Caja();
        caja.setUsuarioId(request.usuarioId());
        caja.setMontoInicial(request.montoInicial());
        caja.setFechaApertura(LocalDateTime.now());
        caja.setEstado("ABIERTA");
        return cajaRepository.save(caja);
    }

    @Transactional
    public Caja cerrar(Long id, CajaCierreRequest request) {
        Caja caja = findById(id);
        if ("CERRADA".equals(caja.getEstado())) {
            throw new BusinessException("La caja ya se encuentra cerrada.");
        }
        caja.setMontoFinal(request.montoFinal());
        caja.setFechaCierre(LocalDateTime.now());
        caja.setEstado("CERRADA");
        return cajaRepository.save(caja);
    }

    /** Filtros opcionales: estado, usuarioId. */
    @Override
    protected Specification<Caja> buildSpecification(Map<String, String> filters) {
        Specification<Caja> spec = null;
        String estado = stringParam(filters, "estado");
        Long usuarioId = longParam(filters, "usuarioId");
        if (estado != null) spec = and(spec, fieldEquals("estado", estado));
        if (usuarioId != null) spec = and(spec, fieldEquals("usuarioId", usuarioId));
        return spec;
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.DESC, "fechaApertura");
    }
}
