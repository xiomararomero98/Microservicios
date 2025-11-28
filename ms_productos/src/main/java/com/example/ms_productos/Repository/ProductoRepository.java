package com.example.ms_productos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_productos.Model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
