package com.farmacia.service;

import com.farmacia.dto.LoginRequest;
import com.farmacia.dto.LoginResponse;
import com.farmacia.dto.UsuarioResponse;
import com.farmacia.entity.Usuario;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.repository.UsuarioRepository;
import com.farmacia.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    /**
     * Valida correo y contraseña contra la base de datos (BCrypt) y emite un JWT.
     * Si las credenciales son incorrectas, AuthenticationManager lanza BadCredentialsException (HTTP 401).
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.password()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.correo());
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(request.correo())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        String token = jwtService.generateToken(userDetails);
        return new LoginResponse(token, "Bearer", jwtService.getExpirationMs(), UsuarioResponse.from(usuario));
    }
}
