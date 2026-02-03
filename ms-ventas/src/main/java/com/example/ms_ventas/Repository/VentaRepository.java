package com.example.ms_ventas.Repository;


import com.example.ms_ventas.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}