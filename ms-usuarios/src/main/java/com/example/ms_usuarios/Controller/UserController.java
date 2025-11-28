package com.example.ms_usuarios.Controller;

import com.example.ms_usuarios.Model.Usuario;
import com.example.ms_usuarios.Service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // ================================
    // LISTAR TODOS
    // ================================
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ================================
    // BUSCAR POR ID
    // ================================
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ================================
    // CREAR USUARIO
    // ================================
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario user) {
        Usuario newUser = service.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // ================================
    // ACTUALIZAR USUARIO
    // ================================
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id,
                                          @RequestBody Usuario user) {
        return ResponseEntity.ok(service.update(id, user));
    }

    // ================================
    // ELIMINAR USUARIO
    // ================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ================================
    // LOGIN
    // ================================
    @PostMapping("/login")
        public ResponseEntity<?> login(@RequestParam String email,
                               @RequestParam String password) {
    try {
        Usuario user = service.login(email, password);
        return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
    // ================================
    // CAMBIAR ROL DE UN USUARIO
    // ================================
    @PutMapping("/{idUsuario}/rol/{idRol}")
    public ResponseEntity<Usuario> cambiarRol(@PathVariable Long idUsuario,
                                              @PathVariable Long idRol) {
        return ResponseEntity.ok(service.cambiarRol(idUsuario, idRol));
    }
}
