package com.example.ms_usuarios.Config;

import com.example.ms_usuarios.Model.Rol;
import com.example.ms_usuarios.Model.Usuario;
import com.example.ms_usuarios.Repository.RolRepository;
import com.example.ms_usuarios.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class LoadDatabase {

    // ==========================================================
    // 1) CARGA DE ROLES
    // ==========================================================
    @Bean
    @Order(1)
    CommandLineRunner cargarRoles(RolRepository rolRepository) {
        return args -> {
            if (rolRepository.count() == 0) {
                System.out.println("==> Cargando Roles...");
                rolRepository.save(new Rol(null, "ADMIN"));
                rolRepository.save(new Rol(null, "CLIENTE"));
            } else {
                System.out.println("Los roles ya existen.");
            }
        };
    }

    // ==========================================================
    // 2) CARGA DE USUARIOS (CON CONTRASEÑAS ENCRIPTADAS)
    // ==========================================================
    @Bean
    @Order(2)
    CommandLineRunner cargarUsuarios(UserRepository userRepository,
                                     RolRepository rolRepository) {
        return args -> {

            if (userRepository.count() == 0) {
                System.out.println("==> Cargando Usuarios...");

                Rol admin = rolRepository.findByNombre("ADMIN").orElse(null);
                Rol cliente = rolRepository.findByNombre("CLIENTE").orElse(null);

                if (admin != null && cliente != null) {

                    // ENCRIPTAR PASSWORDS
                    String passAdmin = BCrypt.hashpw("123456", BCrypt.gensalt());
                    String passCliente = BCrypt.hashpw("654321", BCrypt.gensalt());

                    Usuario u1 = new Usuario(
                            null,
                            "Xiomara",
                            "Admin",
                            "admin@discoclub.cl",
                            passAdmin,
                            "99999999",
                            "Santiago",
                            admin
                    );

                    Usuario u2 = new Usuario(
                            null,
                            "Laura",
                            "Cliente",
                            "cliente@discoclub.cl",
                            passCliente,
                            "88888888",
                            "La Florida",
                            cliente
                    );

                    userRepository.save(u1);
                    userRepository.save(u2);

                    System.out.println("Usuarios precargados correctamente.");

                } else {
                    System.out.println("No se encontraron roles para asignar a los usuarios.");
                }

            } else {
                System.out.println("Los usuarios ya existen.");
            }
        };
    }
}
