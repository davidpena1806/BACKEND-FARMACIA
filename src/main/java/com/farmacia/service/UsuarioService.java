package com.farmacia.service;

import com.farmacia.dto.UsuarioRequest;
import com.farmacia.dto.UsuarioResponse;
import com.farmacia.entity.Rol;
import com.farmacia.entity.Usuario;
import com.farmacia.exception.BusinessException;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.repository.RolRepository;
import com.farmacia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAll() {
        return usuarioRepository.findAll(Sort.by("id")).stream().map(UsuarioResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        return UsuarioResponse.from(obtener(id));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findByCorreo(String correo) {
        return usuarioRepository.findByCorreoIgnoreCase(correo)
                .map(UsuarioResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
    }

    @Transactional
    public UsuarioResponse create(UsuarioRequest request) {
        if (request.password() == null || request.password().isBlank()) {
            throw new BusinessException("La contraseña es obligatoria.");
        }
        if (usuarioRepository.existsByCorreoIgnoreCase(request.correo())) {
            throw new BusinessException("Ya existe un usuario con el correo " + request.correo() + ".");
        }
        Usuario usuario = new Usuario();
        aplicarDatos(usuario, request);
        usuario.setPasswordHash(passwordEncoder.encode(request.password())); // BCrypt
        usuario.setFechaRegistro(LocalDateTime.now());
        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }

    /** Si la contraseña viene vacía en la edición, se conserva la actual. */
    @Transactional
    public UsuarioResponse update(Long id, UsuarioRequest request) {
        Usuario usuario = obtener(id);
        if (usuarioRepository.existsByCorreoIgnoreCaseAndIdNot(request.correo(), id)) {
            throw new BusinessException("Ya existe otro usuario con el correo " + request.correo() + ".");
        }
        aplicarDatos(usuario, request);
        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        }
        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }

    /** Baja lógica. */
    @Transactional
    public void delete(Long id) {
        Usuario usuario = obtener(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private Usuario obtener(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con Id " + id + " no encontrado."));
    }

    private void aplicarDatos(Usuario usuario, UsuarioRequest request) {
        Rol rol = rolRepository.findById(request.rolId())
                .orElseThrow(() -> new BusinessException("El rol indicado no existe."));
        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setCorreo(request.correo());
        usuario.setActivo(request.activo() == null || request.activo());
        usuario.setRolId(rol.getId());
        usuario.setRol(rol);
    }
}
