package com.example.ms_productos.Controller;

import com.example.ms_productos.Model.Producto;
import com.example.ms_productos.Service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id,
                                           @RequestBody Producto producto) {
        return ResponseEntity.ok(service.update(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
