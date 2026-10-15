package com.farmacia.dto;

import com.farmacia.entity.Rol;
import com.farmacia.entity.Usuario;

import java.time.LocalDateTime;

/** Usuario expuesto por la API: NUNCA incluye la contraseña ni su hash. */
public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String correo,
        Boolean activo,
        LocalDateTime fechaRegistro,
        Long rolId,
        Rol rol) {

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getApellido(), u.getCorreo(),
                u.getActivo(), u.getFechaRegistro(), u.getRolId(), u.getRol());
    }
}
