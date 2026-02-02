package com.example.ms_pagos.Controller;

import com.example.ms_pagos.Model.Pagos;
import com.example.ms_pagos.Service.PagosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagosController {

    private final PagosService pagosService;

    public PagosController(PagosService pagosService) {
        this.pagosService = pagosService;
    }

    // POST http://localhost:8083/pagos
    @PostMapping
    public ResponseEntity<Pagos> pagar(@RequestBody Pagos pago) {
        return ResponseEntity.ok(pagosService.procesarPago(pago));
    }

    // GET http://localhost:8083/pagos
    @GetMapping
    public ResponseEntity<List<Pagos>> listar() {
        return ResponseEntity.ok(pagosService.listarTodos());
    }

    // GET http://localhost:8083/pagos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Pagos> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pagosService.buscarPorId(id));
    }

    // GET http://localhost:8083/pagos/venta/{ventaId}
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<Pagos>> listarPorVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(pagosService.listarPorVentaId(ventaId));
    }
}
