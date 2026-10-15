package com.farmacia.dto;

public record LoginResponse(String token, String tipo, long expiraEnMs, UsuarioResponse usuario) {
}
