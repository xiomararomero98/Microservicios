package com.example.ms_usuarios.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_usuarios.Model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    // Busca el rol sin importar mayúsculas/minúsculas
    Optional<Rol> findByNombreIgnoreCase(String nombre);
}
