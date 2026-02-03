package com.example.ms_productos.Service;

import com.example.ms_productos.Model.Producto;
import com.example.ms_productos.Repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAll_devuelveLista() {
        when(repository.findAll())
                .thenReturn(Arrays.asList(new Producto(), new Producto()));

        var productos = service.getAll();

        assertEquals(2, productos.size());
        verify(repository).findAll();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getById_ok() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Coca Cola");

        when(repository.findById(1L))
                .thenReturn(Optional.of(p));

        Producto result = service.getById(1L);

        assertEquals("Coca Cola", result.getNombre());
        verify(repository).findById(1L);
    }

    @Test
    void getById_noExiste_lanzaError() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getById(1L));

        assertTrue(ex.getMessage().toLowerCase().contains("no"));
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void create_ok() {
        Producto p = new Producto();
        p.setNombre("Fanta");

        when(repository.save(p)).thenReturn(p);

        Producto result = service.create(p);

        assertEquals("Fanta", result.getNombre());
        verify(repository).save(p);
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void update_ok() {
        Producto original = new Producto();
        original.setId(1L);
        original.setNombre("Pepsi");
        original.setStock(10);

        Producto actualizado = new Producto();
        actualizado.setNombre("Pepsi Max");
        actualizado.setStock(8);

        when(repository.findById(1L))
                .thenReturn(Optional.of(original));

        when(repository.save(any(Producto.class)))
                .thenAnswer(i -> i.getArgument(0));

        Producto result = service.update(1L, actualizado);

        assertEquals("Pepsi Max", result.getNombre());
        assertEquals(8, result.getStock());
        verify(repository).save(any(Producto.class));
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void delete_ok() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_noExiste_lanzaError() {
        when(repository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.delete(1L));

        assertTrue(ex.getMessage().toLowerCase().contains("no"));
    }

    // =========================
    // DESCONTAR STOCK (CLAVE PARA VENTAS)
    // =========================
    @Test
    void descontarStock_ok() {
        Producto p = new Producto();
        p.setId(1L);
        p.setStock(10);

        when(repository.findById(1L))
                .thenReturn(Optional.of(p));

        when(repository.save(any(Producto.class)))
                .thenAnswer(i -> i.getArgument(0));

        Producto result = service.descontarStock(1L, 3);

        assertEquals(7, result.getStock());
        verify(repository).save(any(Producto.class));
    }

    @Test
    void descontarStock_stockInsuficiente_lanzaError() {
        Producto p = new Producto();
        p.setId(1L);
        p.setStock(1);

        when(repository.findById(1L))
                .thenReturn(Optional.of(p));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.descontarStock(1L, 5));

        assertTrue(ex.getMessage().toLowerCase().contains("stock"));
        verify(repository, never()).save(any());
    }

    @Test
    void descontarStock_cantidadInvalida_lanzaError() {
        Producto p = new Producto();
        p.setId(1L);
        p.setStock(10);

        when(repository.findById(1L))
                .thenReturn(Optional.of(p));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.descontarStock(1L, 0));

        assertTrue(ex.getMessage().toLowerCase().contains("cantidad"));
        verify(repository, never()).save(any());
    }
}
