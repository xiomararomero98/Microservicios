package com.example.ms_productos.Config;


import com.example.ms_productos.Model.Producto;
import com.example.ms_productos.Repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class LoadDataBase {

    // ==========================================================
    // 1) Cargar Productos Iniciales
    // ==========================================================
    @Bean
    @Order(1)
    CommandLineRunner cargarProductos(ProductoRepository productoRepository) {
        return args -> {

            if (productoRepository.count() == 0) {
                System.out.println("==> Cargando Productos...");

                // ---------- BEBIDAS ----------
                Producto p1 = new Producto();
                p1.setNombre("Pack Cervezas 12u");
                p1.setDescripcion("Pack de 12 cervezas premium, ideal para compartir en la previa.");
                p1.setPrecio(8990.0);
                p1.setStock(30);
                p1.setImagen("/img/cervezas.png");

                Producto p2 = new Producto();
                p2.setNombre("Whisky Premium 750ml");
                p2.setDescripcion("Whisky importado de alta calidad. Perfecto para mesas VIP.");
                p2.setPrecio(24900.0);
                p2.setStock(10);
                p2.setImagen("/img/whiskys.png");

                // ---------- ENTRADAS / EVENTOS ----------
                Producto p3 = new Producto();
                p3.setNombre("Entrada General");
                p3.setDescripcion("Acceso general al evento nocturno. Cupos limitados.");
                p3.setPrecio(5000.0);
                p3.setStock(100);
                p3.setImagen("/img/entradas.png");

                Producto p4 = new Producto();
                p4.setNombre("Fiesta de Disfraces");
                p4.setDescripcion("Evento temático: ven con tu mejor outfit. Premios al mejor disfraz.");
                p4.setPrecio(15000.0);
                p4.setStock(80);
                p4.setImagen("/img/disfraces.png");

                Producto p5 = new Producto();
                p5.setNombre("Noche Reggaetón");
                p5.setDescripcion("Reggaetón all night: DJ en vivo y show sorpresa. No te lo pierdas.");
                p5.setPrecio(12000.0);
                p5.setStock(120);
                p5.setImagen("/img/regueton.png");

                productoRepository.save(p1);
                productoRepository.save(p2);
                productoRepository.save(p3);
                productoRepository.save(p4);
                productoRepository.save(p5);

                System.out.println("Productos precargados correctamente.");

            } else {
                System.out.println("Los productos ya existen.");
            }
        };
    }
}
