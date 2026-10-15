package com.farmacia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos para crear/editar un usuario. La contraseña llega en texto plano y se cifra con BCrypt en el servicio. */
public record UsuarioRequest(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank @Email String correo,
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password,
        Boolean activo,
        @NotNull Long rolId) {
}
