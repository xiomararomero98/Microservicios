package com.example.ms_productos.Controller;
import com.example.ms_productos.Model.Producto;
import com.example.ms_productos.Service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAll() throws Exception {
        Producto p1 = new Producto();
        p1.setId(1L);
        p1.setNombre("Coca Cola");

        Producto p2 = new Producto();
        p2.setId(2L);
        p2.setNombre("Fanta");

        when(service.getAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetById() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Sprite");

        when(service.getById(1L)).thenReturn(p);

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Sprite"));
    }

    @Test
    void testCreate() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Agua");

        when(service.create(any(Producto.class))).thenReturn(p);

        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdate() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Pepsi Max");

        when(service.update(eq(1L), any(Producto.class))).thenReturn(p);

        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pepsi Max"));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());
    }
}