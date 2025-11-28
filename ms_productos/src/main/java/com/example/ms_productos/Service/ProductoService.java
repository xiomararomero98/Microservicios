package com.example.ms_productos.Service;

import com.example.ms_productos.Model.Producto;
import com.example.ms_productos.Repository.ProductoRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> getAll() {
        return repository.findAll();
    }

    public Producto getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto create(Producto producto) {
        return repository.save(producto);
    }

    public Producto update(Long id, Producto producto) {
        Producto dbProducto = getById(id);

        dbProducto.setNombre(producto.getNombre());
        dbProducto.setDescripcion(producto.getDescripcion());
        dbProducto.setPrecio(producto.getPrecio());
        dbProducto.setStock(producto.getStock());
        dbProducto.setImagen(producto.getImagen());

        return repository.save(dbProducto);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No existe el producto");
        }
        repository.deleteById(id);
    }
}
