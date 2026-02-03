package com.example.ms_ventas.WebClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(@Value("${productos-service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Map<String, Object> getProducto(Long id) {
        return webClient.get()
                .uri("/productos/{id}", id)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public void descontarStock(Long id, int cantidad) {
        webClient.put()
                .uri("/productos/{id}/decrementar/{cantidad}", id, cantidad)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
