package com.example.ms_pagos.Repository;

import com.example.ms_pagos.Model.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagosRepository extends JpaRepository<Pagos, Long> {
    List<Pagos> findByVentaId(Long ventaId);
}
