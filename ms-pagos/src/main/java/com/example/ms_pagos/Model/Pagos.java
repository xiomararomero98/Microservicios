package com.example.ms_pagos.Model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Para relacionar con la venta (ms-ventas) o con un "checkout" de la app
    @Column(nullable = false)
    private Long ventaId;

    // Monto total a pagar
    @Column(nullable = false)
    private Double monto;

    // Método simulado: TARJETA, TRANSFERENCIA, etc
    @Column(nullable = false)
    private String metodo;

    // Estado: APROBADO, RECHAZADO, PENDIENTE
    @Column(nullable = false)
    private String estado;

    // Fecha/hora del pago
    @Column(nullable = false)
    private LocalDateTime fecha;

    // Código/folio para "comprobante"
    @Column(nullable = false, unique = true)
    private String referencia;
}
