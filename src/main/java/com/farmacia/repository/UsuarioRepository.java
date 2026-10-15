package com.farmacia.repository;

import com.farmacia.entity.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario> {
    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Long id);
}
