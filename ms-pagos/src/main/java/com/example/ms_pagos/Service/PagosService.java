package com.example.ms_pagos.Service;

import com.example.ms_pagos.Model.Pagos;
import com.example.ms_pagos.Repository.PagosRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PagosService {

    private final PagosRepository pagosRepository;

    public PagosService(PagosRepository pagosRepository) {
        this.pagosRepository = pagosRepository;
    }

    /**
     * Simulación simple:
     * - Si monto <= 0 => RECHAZADO
     * - Si metodo vacío => RECHAZADO
     * - Si monto > 0 y metodo ok => APROBADO
     */
    public Pagos procesarPago(Pagos pago) {
        if (pago.getVentaId() == null) {
            throw new IllegalArgumentException("ventaId es obligatorio");
        }
        if (pago.getMonto() == null || pago.getMonto() <= 0) {
            pago.setEstado("RECHAZADO");
        } else if (pago.getMetodo() == null || pago.getMetodo().isBlank()) {
            pago.setEstado("RECHAZADO");
        } else {
            pago.setEstado("APROBADO");
        }

        pago.setFecha(LocalDateTime.now());

        // folio / referencia única
        if (pago.getReferencia() == null || pago.getReferencia().isBlank()) {
            pago.setReferencia("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        return pagosRepository.save(pago);
    }

    public List<Pagos> listarTodos() {
        return pagosRepository.findAll();
    }

    public Pagos buscarPorId(Long id) {
        return pagosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con id: " + id));
    }

    public List<Pagos> listarPorVentaId(Long ventaId) {
        return pagosRepository.findByVentaId(ventaId);
    }
}
