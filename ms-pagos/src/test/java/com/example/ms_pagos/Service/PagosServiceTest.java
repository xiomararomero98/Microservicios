package com.example.ms_pagos.Service;


import com.example.ms_pagos.Model.Pagos;
import com.example.ms_pagos.Repository.PagosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagosServiceTest {

    @Mock
    private PagosRepository pagosRepository;

    @InjectMocks
    private PagosService pagosService;

    private Pagos basePago;

    @BeforeEach
    void setUp() {
        basePago = new Pagos();
        basePago.setVentaId(10L);
        basePago.setMonto(15000.0);
        basePago.setMetodo("TARJETA");
        basePago.setEstado(null);
        basePago.setReferencia(null);
        basePago.setFecha(null);
    }

    // ==========================================================
    // PROCESAR PAGO - APROBADO
    // ==========================================================
    @Test
    void procesarPago_cuandoMontoYMetodoValidos_seteaAprobado_yGuarda() {

        when(pagosRepository.save(any(Pagos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pagos result = pagosService.procesarPago(basePago);

        assertNotNull(result.getFecha(), "Debe setear fecha");
        assertNotNull(result.getReferencia(), "Debe generar referencia si venía null");
        assertTrue(result.getReferencia().startsWith("PAY-"), "Referencia debe empezar con PAY-");
        assertEquals("APROBADO", result.getEstado(), "Debe quedar APROBADO");

        verify(pagosRepository, times(1)).save(any(Pagos.class));
    }

    // ==========================================================
    // PROCESAR PAGO - RECHAZADO POR MONTO
    // ==========================================================
    @Test
    void procesarPago_cuandoMontoEsCero_rechaza() {

        basePago.setMonto(0.0);

        when(pagosRepository.save(any(Pagos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pagos result = pagosService.procesarPago(basePago);

        assertEquals("RECHAZADO", result.getEstado());
        verify(pagosRepository).save(any(Pagos.class));
    }

    @Test
    void procesarPago_cuandoMontoEsNegativo_rechaza() {

        basePago.setMonto(-1.0);

        when(pagosRepository.save(any(Pagos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pagos result = pagosService.procesarPago(basePago);

        assertEquals("RECHAZADO", result.getEstado());
        verify(pagosRepository).save(any(Pagos.class));
    }

    // ==========================================================
    // PROCESAR PAGO - RECHAZADO POR METODO VACIO
    // ==========================================================
    @Test
    void procesarPago_cuandoMetodoEsVacio_rechaza() {

        basePago.setMetodo("   ");

        when(pagosRepository.save(any(Pagos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pagos result = pagosService.procesarPago(basePago);

        assertEquals("RECHAZADO", result.getEstado());
        verify(pagosRepository).save(any(Pagos.class));
    }

    // ==========================================================
    // PROCESAR PAGO - EXCEPCION POR VENTAID NULL
    // ==========================================================
    @Test
    void procesarPago_cuandoVentaIdEsNull_lanzaExcepcion_yNoGuarda() {

        basePago.setVentaId(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pagosService.procesarPago(basePago)
        );

        assertEquals("ventaId es obligatorio", ex.getMessage());
        verify(pagosRepository, never()).save(any(Pagos.class));
    }

    // ==========================================================
    // LISTAR TODOS
    // ==========================================================
    @Test
    void listarTodos_devuelveLista() {

        when(pagosRepository.findAll()).thenReturn(List.of(new Pagos(), new Pagos()));

        List<Pagos> result = pagosService.listarTodos();

        assertEquals(2, result.size());
        verify(pagosRepository).findAll();
    }

    // ==========================================================
    // BUSCAR POR ID
    // ==========================================================
    @Test
    void buscarPorId_cuandoExiste_devuelvePago() {

        Pagos p = new Pagos();
        p.setId(1L);

        when(pagosRepository.findById(1L)).thenReturn(Optional.of(p));

        Pagos result = pagosService.buscarPorId(1L);

        assertEquals(1L, result.getId());
        verify(pagosRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {

        when(pagosRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pagosService.buscarPorId(99L)
        );

        assertTrue(ex.getMessage().contains("Pago no encontrado"));
        verify(pagosRepository).findById(99L);
    }

    // ==========================================================
    // LISTAR POR VENTA ID
    // ==========================================================
    @Test
    void listarPorVentaId_devuelveLista() {

        when(pagosRepository.findByVentaId(10L)).thenReturn(List.of(new Pagos()));

        List<Pagos> result = pagosService.listarPorVentaId(10L);

        assertEquals(1, result.size());
        verify(pagosRepository).findByVentaId(10L);
    }
}
