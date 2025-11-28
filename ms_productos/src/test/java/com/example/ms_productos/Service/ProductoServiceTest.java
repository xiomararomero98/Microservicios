package com.example.ms_productos.Service;

import com.example.ms_productos.Model.Producto;
import com.example.ms_productos.Repository.ProductoRepository;
import com.example.ms_productos.Service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(new Producto(), new Producto()));

        var productos = service.getAll();

        assertEquals(2, productos.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Coca Cola");

        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Producto result = service.getById(1L);

        assertEquals("Coca Cola", result.getNombre());
        verify(repository).findById(1L);
    }

    @Test
    void testCreate() {
        Producto p = new Producto();
        p.setNombre("Fanta");

        when(repository.save(p)).thenReturn(p);

        Producto result = service.create(p);

        assertEquals("Fanta", result.getNombre());
        verify(repository).save(p);
    }

    @Test
    void testUpdate() {
        Producto original = new Producto();
        original.setId(1L);
        original.setNombre("Pepsi");

        Producto actualizado = new Producto();
        actualizado.setNombre("Pepsi Max");

        when(repository.findById(1L)).thenReturn(Optional.of(original));
        when(repository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        Producto result = service.update(1L, actualizado);

        assertEquals("Pepsi Max", result.getNombre());
        verify(repository).save(any(Producto.class));
    }

    @Test
    void testDelete() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}

