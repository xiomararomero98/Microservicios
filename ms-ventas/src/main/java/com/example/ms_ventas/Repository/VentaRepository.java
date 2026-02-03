package com.example.ms_ventas.Repository;


import com.example.ms_ventas.Model.Venta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuarioId(Long usuarioId);
}