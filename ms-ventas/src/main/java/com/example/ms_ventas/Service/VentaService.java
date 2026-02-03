package com.example.ms_ventas.Service;


import com.example.ms_ventas.Model.Venta;
import com.example.ms_ventas.Model.DetalleVenta;
import com.example.ms_ventas.Repository.VentaRepository;
import com.example.ms_ventas.WebClient.ProductoClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoClient productosClient;

    public VentaService(VentaRepository ventaRepository, ProductoClient productosClient) {
        this.ventaRepository = ventaRepository;
        this.productosClient = productosClient;
    }

    public Venta checkout(Venta venta) {
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La venta debe tener al menos 1 producto.");
        }

        venta.setFecha(LocalDateTime.now());

        double total = 0.0;

        // recorremos detalles y validamos con ms-productos
        for (DetalleVenta d : venta.getDetalles()) {
            if (d.getProductoId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productoId es obligatorio.");
            }
            if (d.getCantidad() == null || d.getCantidad() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0.");
            }

            Map<String, Object> producto = productosClient.getProducto(d.getProductoId());
            if (producto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + d.getProductoId());
            }

            // esperados: precio, stock (según tu DTO ProductosDto)
            double precio = Double.parseDouble(producto.get("precio").toString());
            int stock = Integer.parseInt(producto.get("stock").toString());

            if (d.getCantidad() > stock) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stock insuficiente para producto " + d.getProductoId() + ". Stock: " + stock
                );
            }

            d.setPrecioUnitario(precio);
            d.setSubtotal(precio * d.getCantidad());
            total += d.getSubtotal();

            d.setVenta(venta);
        }

        venta.setTotal(total);

         for (DetalleVenta d : venta.getDetalles()) {
             productosClient.descontarStock(d.getProductoId(), d.getCantidad());
         }

        return ventaRepository.save(venta);
    }

    public Venta getById(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
    }
}
