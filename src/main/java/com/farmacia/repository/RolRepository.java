package com.farmacia.repository;

import com.farmacia.entity.Rol;
import java.util.Optional;

public interface RolRepository extends BaseRepository<Rol> {
    Optional<Rol> findByNombreIgnoreCase(String nombre);
}
