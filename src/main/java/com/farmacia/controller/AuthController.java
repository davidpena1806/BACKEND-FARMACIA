package com.farmacia.controller;

import com.farmacia.dto.LoginRequest;
import com.farmacia.dto.LoginResponse;
import com.farmacia.dto.UsuarioResponse;
import com.farmacia.service.AuthService;
import com.farmacia.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    /** Servicio REST de login: valida usuario/password contra la BD (BCrypt) y devuelve un JWT. */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /** Datos del usuario autenticado (requiere token). */
    @GetMapping("/me")
    public UsuarioResponse me(Authentication authentication) {
        return usuarioService.findByCorreo(authentication.getName());
    }
}
