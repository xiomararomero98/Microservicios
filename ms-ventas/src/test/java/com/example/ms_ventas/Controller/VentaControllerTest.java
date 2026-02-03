package com.example.ms_ventas.Controller;

import com.example.ms_ventas.Model.Venta;
import com.example.ms_ventas.Repository.VentaRepository;
import com.example.ms_ventas.Service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
class VentaControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    VentaService ventaService;

    // 👇 si tu controller NO usa repository, puedes borrar esto.
    // Si tu controller lo inyecta, es obligatorio mockearlo para que el contexto cargue.
    @MockBean
    VentaRepository ventaRepository;

    @Test
    void checkout_ok_devuelve200() throws Exception {
        Venta fake = new Venta();
        fake.setId(1L);
        fake.setUsuarioId(5L);
        fake.setTotal(2000.0);
        fake.setFecha(LocalDateTime.now());
        fake.setDetalles(List.of());

        when(ventaService.checkout(any(Venta.class))).thenReturn(fake);

        String body = """
        {
          "usuarioId": 5,
          "detalles": [
            { "productoId": 1, "cantidad": 2 }
          ]
        }
        """;

        mvc.perform(post("/ventas/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(5))
                .andExpect(jsonPath("$.total").value(2000.0));
    }

    @Test
    void checkout_error_stockInsuficiente_devuelve400() throws Exception {
        // tu service lanza ResponseStatusException en tu implementación real:
        when(ventaService.checkout(any(Venta.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente"));

        String body = """
        {
          "usuarioId": 5,
          "detalles": [
            { "productoId": 1, "cantidad": 99 }
          ]
        }
        """;

        mvc.perform(post("/ventas/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
