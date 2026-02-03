package com.example.ms_ventas.Controller;

import com.example.ms_ventas.Model.Venta;
import com.example.ms_ventas.Repository.VentaRepository;
import com.example.ms_ventas.Service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Ventas",
        description = "Microservicio de ventas (checkout). Puerto: 8083. Se integra con ms-productos (8082) para validar y descontar stock."
)
@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final VentaRepository ventaRepository;

    public VentaController(VentaService ventaService, VentaRepository ventaRepository) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
    }

    // =========================
    // CHECKOUT
    // =========================
    @Operation(
            summary = "Checkout: crea una venta",
            description = """
                    Crea una venta a partir de una lista de productos.
                    Valida stock contra ms-productos (puerto 8082) y descuenta stock por cada item.
                    Retorna la venta creada con su total y detalles.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venta creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos / stock insuficiente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping("/checkout")
    public ResponseEntity<Venta> checkout(@RequestBody Venta venta) {
        Venta created = ventaService.checkout(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =========================
    // GET BY ID
    // =========================
    @Operation(
            summary = "Obtener venta por ID",
            description = "Retorna una venta por su ID. Si no existe, devuelve 404."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.getById(id));
    }

    // =========================
    // LIST
    // =========================
    @Operation(
            summary = "Listar ventas",
            description = "Obtiene todas las ventas registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Venta>> list() {
        return ResponseEntity.ok(ventaRepository.findAll());
    }
}
