package com.example.ms_pagos.Controller;


import com.example.ms_pagos.Model.Pagos;
import com.example.ms_pagos.Service.PagosService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagosController.class)
@ExtendWith(SpringExtension.class)
public class PagosControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private PagosService pagosService;

    @Test
    void pagar_retornaPagoAprobado() throws Exception {

        // Request JSON (lo que manda el cliente)
        Pagos request = new Pagos();
        request.setVentaId(5L);
        request.setMonto(12000.0);
        request.setMetodo("TARJETA");

        // Respuesta del service (mock)
        Pagos pagoMock = new Pagos();
        pagoMock.setId(1L);
        pagoMock.setVentaId(5L);
        pagoMock.setMonto(12000.0);
        pagoMock.setMetodo("TARJETA");
        pagoMock.setEstado("APROBADO");
        pagoMock.setReferencia("PAY-ABCD1234");
        pagoMock.setFecha(LocalDateTime.now());

        when(pagosService.procesarPago(any(Pagos.class))).thenReturn(pagoMock);

        mockMvc.perform(
                post("/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.ventaId").value(5))
        .andExpect(jsonPath("$.monto").value(12000.0))
        .andExpect(jsonPath("$.estado").value("APROBADO"))
        .andExpect(jsonPath("$.referencia").value("PAY-ABCD1234"));
    }

    @Test
    void listar_retornaLista() throws Exception {
        Pagos p1 = new Pagos();
        p1.setId(1L);
        p1.setVentaId(5L);
        p1.setMonto(1000.0);
        p1.setMetodo("TARJETA");
        p1.setEstado("APROBADO");

        when(pagosService.listarTodos()).thenReturn(List.of(p1));

        mockMvc.perform(get("/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ventaId").value(5));
    }

    @Test
    void obtener_retornaPago() throws Exception {
        Pagos p = new Pagos();
        p.setId(10L);
        p.setVentaId(99L);
        p.setMonto(5000.0);
        p.setMetodo("TRANSFERENCIA");
        p.setEstado("APROBADO");

        when(pagosService.buscarPorId(eq(10L))).thenReturn(p);

        mockMvc.perform(get("/pagos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.ventaId").value(99));
    }

    @Test
    void listarPorVenta_retornaLista() throws Exception {
        Pagos p = new Pagos();
        p.setId(1L);
        p.setVentaId(7L);
        p.setMonto(1500.0);
        p.setMetodo("TARJETA");
        p.setEstado("APROBADO");

        when(pagosService.listarPorVentaId(eq(7L))).thenReturn(List.of(p));

        mockMvc.perform(get("/pagos/venta/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ventaId").value(7));
    }
}
