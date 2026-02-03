package com.example.ms_ventas.Service;

import com.example.ms_ventas.Model.DetalleVenta;
import com.example.ms_ventas.Model.Venta;
import com.example.ms_ventas.Repository.VentaRepository;
import com.example.ms_ventas.WebClient.ProductoClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaServiceTest {

    @Test
    void checkout_ok_calculaTotal_y_guardaVenta() {
        VentaRepository ventaRepo = mock(VentaRepository.class);
        ProductoClient productoClient = mock(ProductoClient.class);

        VentaService service = new VentaService(ventaRepo, productoClient);

        // Producto mock (ms-productos)
        when(productoClient.getProducto(1L)).thenReturn(Map.of(
                "id", 1,
                "precio", 1000.0,
                "stock", 10,
                "nombre", "Perfume"
        ));

        // save devuelve el mismo objeto que se le pasa
        when(ventaRepo.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        // venta de entrada
        Venta venta = new Venta();
        venta.setUsuarioId(5L);

        DetalleVenta det = new DetalleVenta();
        det.setProductoId(1L);
        det.setCantidad(2);

        venta.setDetalles(List.of(det));

        // ejecutar
        Venta result = service.checkout(venta);

        // asserts
        assertNotNull(result);
        assertEquals(5L, result.getUsuarioId());
        assertEquals(2000.0, result.getTotal());
        assertNotNull(result.getFecha());
        assertEquals(1, result.getDetalles().size());
        assertEquals(1000.0, result.getDetalles().get(0).getPrecioUnitario());
        assertEquals(2000.0, result.getDetalles().get(0).getSubtotal());

        verify(productoClient, times(1)).getProducto(1L);
        verify(ventaRepo, times(1)).save(any(Venta.class));
    }

    @Test
    void checkout_stockInsuficiente_devuelve400_y_noGuarda() {
        VentaRepository ventaRepo = mock(VentaRepository.class);
        ProductoClient productoClient = mock(ProductoClient.class);

        VentaService service = new VentaService(ventaRepo, productoClient);

        when(productoClient.getProducto(1L)).thenReturn(Map.of(
                "id", 1,
                "precio", 1000.0,
                "stock", 1
        ));

        Venta venta = new Venta();
        venta.setUsuarioId(5L);

        DetalleVenta det = new DetalleVenta();
        det.setProductoId(1L);
        det.setCantidad(2);

        venta.setDetalles(List.of(det));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.checkout(venta));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().toLowerCase().contains("stock"));

        verify(ventaRepo, never()).save(any());
    }

    @Test
    void checkout_sinDetalles_devuelve400() {
        VentaRepository ventaRepo = mock(VentaRepository.class);
        ProductoClient productoClient = mock(ProductoClient.class);

        VentaService service = new VentaService(ventaRepo, productoClient);

        Venta venta = new Venta();
        venta.setUsuarioId(5L);
        venta.setDetalles(List.of()); // vacío

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.checkout(venta));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().toLowerCase().contains("al menos"));

        verify(ventaRepo, never()).save(any());
    }

    @Test
    void checkout_productoIdNull_devuelve400() {
        VentaRepository ventaRepo = mock(VentaRepository.class);
        ProductoClient productoClient = mock(ProductoClient.class);

        VentaService service = new VentaService(ventaRepo, productoClient);

        Venta venta = new Venta();
        venta.setUsuarioId(5L);

        DetalleVenta det = new DetalleVenta();
        det.setProductoId(null);
        det.setCantidad(1);

        venta.setDetalles(List.of(det));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.checkout(venta));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().toLowerCase().contains("productoid"));

        verify(ventaRepo, never()).save(any());
        verify(productoClient, never()).getProducto(anyLong());
    }

    @Test
    void checkout_cantidadInvalida_devuelve400() {
        VentaRepository ventaRepo = mock(VentaRepository.class);
        ProductoClient productoClient = mock(ProductoClient.class);

        VentaService service = new VentaService(ventaRepo, productoClient);

        Venta venta = new Venta();
        venta.setUsuarioId(5L);

        DetalleVenta det = new DetalleVenta();
        det.setProductoId(1L);
        det.setCantidad(0);

        venta.setDetalles(List.of(det));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.checkout(venta));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().toLowerCase().contains("cantidad"));

        verify(ventaRepo, never()).save(any());
        verify(productoClient, never()).getProducto(anyLong());
    }
}
