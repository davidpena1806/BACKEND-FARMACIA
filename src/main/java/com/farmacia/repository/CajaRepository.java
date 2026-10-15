package com.farmacia.repository;

import com.farmacia.entity.Caja;

public interface CajaRepository extends BaseRepository<Caja> {
    boolean existsByUsuarioIdAndEstado(Long usuarioId, String estado);
}
