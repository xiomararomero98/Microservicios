package com.example.ms_productos.Controller;

import com.example.ms_productos.Model.Producto;
import com.example.ms_productos.Service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Productos",
    description = "Microservicio de gestión de productos. Puerto: 8082"
)
@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // =========================
    // GET ALL
    // =========================
    @Operation(
        summary = "Listar productos",
        description = "Obtiene todos los productos disponibles desde ms-productos (puerto 8082)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Producto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // =========================
    // GET BY ID
    // =========================
    @Operation(
        summary = "Obtener producto por ID",
        description = "Busca un producto específico por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // =========================
    // CREATE
    // =========================
    @Operation(
        summary = "Crear producto",
        description = "Crea un nuevo producto en el sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(producto));
    }

    // =========================
    // UPDATE
    // =========================
    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza los datos de un producto existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(
            @PathVariable Long id,
            @RequestBody Producto producto
    ) {
        return ResponseEntity.ok(service.update(id, producto));
    }

    // =========================
    // DELETE
    // =========================
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // DESCONTAR STOCK (CLAVE PARA VENTAS)
    // =========================
    @Operation(
        summary = "Descontar stock",
        description = """
            Descuenta stock de un producto.
            Este endpoint es utilizado por el microservicio ms-ventas (puerto 8083)
            durante el proceso de checkout.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock descontado correctamente"),
        @ApiResponse(responseCode = "400", description = "Cantidad inválida o stock insuficiente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}/decrementar/{cantidad}")
    public ResponseEntity<Producto> descontarStock(
            @PathVariable Long id,
            @PathVariable int cantidad
    ) {
        return ResponseEntity.ok(service.descontarStock(id, cantidad));
    }
}
