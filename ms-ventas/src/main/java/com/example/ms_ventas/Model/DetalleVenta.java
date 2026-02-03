package com.example.ms_ventas.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "venta_detalle")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // producto comprado
    private Long productoId;

    private Integer cantidad;

    // snapshot del precio en el momento de compra
    private Double precioUnitario;

    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    @JsonBackReference
    private Venta venta;
}
