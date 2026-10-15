package com.farmacia.security;

import com.farmacia.entity.Usuario;
import com.farmacia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Carga el usuario desde la base de datos para que Spring Security valide el password (BCrypt). */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        String rol = usuario.getRol() != null ? usuario.getRol().getNombre() : "USER";
        String authority = "ROLE_" + rol.trim().toUpperCase().replace(' ', '_');

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority(authority)))
                .disabled(!Boolean.TRUE.equals(usuario.getActivo()))
                .build();
    }
}
