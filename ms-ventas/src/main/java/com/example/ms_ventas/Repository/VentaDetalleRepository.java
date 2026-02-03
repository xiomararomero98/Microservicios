package com.example.ms_ventas.Repository;

import com.example.ms_ventas.Model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaDetalleRepository extends JpaRepository<DetalleVenta, Long> {
}
