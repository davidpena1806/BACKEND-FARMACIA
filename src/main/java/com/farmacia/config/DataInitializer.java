package com.farmacia.config;

import com.farmacia.entity.Rol;
import com.farmacia.entity.Usuario;
import com.farmacia.repository.RolRepository;
import com.farmacia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/** Datos iniciales: roles ADMIN y VENDEDOR, y un usuario administrador con la contraseña cifrada con BCrypt. */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Rol admin = obtenerOCrearRol("ADMIN", "Administrador del sistema");
        obtenerOCrearRol("VENDEDOR", "Personal de ventas y caja");

        if (usuarioRepository.count() == 0) {
            Usuario usuario = new Usuario();
            usuario.setNombre("Administrador");
            usuario.setApellido("Sistema");
            usuario.setCorreo("admin@farmacia.com");
            usuario.setPasswordHash(passwordEncoder.encode("Admin123*"));
            usuario.setActivo(true);
            usuario.setFechaRegistro(LocalDateTime.now());
            usuario.setRolId(admin.getId());
            usuarioRepository.save(usuario);
            log.info("Usuario inicial creado -> correo: admin@farmacia.com | contraseña: Admin123*");
        }
    }

    private Rol obtenerOCrearRol(String nombre, String descripcion) {
        return rolRepository.findByNombreIgnoreCase(nombre).orElseGet(() -> {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rol.setDescripcion(descripcion);
            return rolRepository.save(rol);
        });
    }
}
