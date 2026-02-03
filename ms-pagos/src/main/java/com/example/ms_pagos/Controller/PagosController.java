package com.example.ms_pagos.Controller;

import com.example.ms_pagos.Model.Pagos;
import com.example.ms_pagos.Service.PagosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Pagos",
        description = "Microservicio de pagos. Puerto: 8083. Gestiona pagos asociados a ventas."
)
@RestController
@RequestMapping("/pagos")
public class PagosController {

    private final PagosService pagosService;

    public PagosController(PagosService pagosService) {
        this.pagosService = pagosService;
    }

    // ================================
    // PROCESAR PAGO
    // ================================
    @Operation(
            summary = "Procesar pago",
            description = "Registra un pago asociado a una venta. Simula el proceso de pago."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago procesado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del pago inválidos"),
            @ApiResponse(responseCode = "404", description = "Venta asociada no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar el pago")
    })
    @PostMapping
    public ResponseEntity<Pagos> pagar(@RequestBody Pagos pago) {
        Pagos creado = pagosService.procesarPago(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ================================
    // LISTAR TODOS LOS PAGOS
    // ================================
    @Operation(
            summary = "Listar pagos",
            description = "Obtiene todos los pagos registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Pagos>> listar() {
        return ResponseEntity.ok(pagosService.listarTodos());
    }

    // ================================
    // OBTENER PAGO POR ID
    // ================================
    @Operation(
            summary = "Obtener pago por ID",
            description = "Busca un pago específico por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pagos> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pagosService.buscarPorId(id));
    }

    // ================================
    // LISTAR PAGOS POR VENTA
    // ================================
    @Operation(
            summary = "Listar pagos por venta",
            description = "Obtiene todos los pagos asociados a una venta específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagos de la venta obtenidos correctamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<Pagos>> listarPorVenta(@PathVariable Long ventaId) {
        return ResponseEntity.ok(pagosService.listarPorVentaId(ventaId));
    }
}
