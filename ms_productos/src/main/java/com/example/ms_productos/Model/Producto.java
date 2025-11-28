package com.example.ms_productos.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private Double precio;

    private Integer stock;

    private String imagen; // URL o nombre del archivo
    
}