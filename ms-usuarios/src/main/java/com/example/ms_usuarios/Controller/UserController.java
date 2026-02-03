package com.example.ms_usuarios.Controller;

import com.example.ms_usuarios.Model.Usuario;
import com.example.ms_usuarios.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Usuarios",
        description = "Microservicio de gestión de usuarios. Puerto: 8081. Maneja autenticación, perfiles y roles."
)
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // ================================
    // LISTAR TODOS
    // ================================
    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene todos los usuarios registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ================================
    // BUSCAR POR ID
    // ================================
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Busca un usuario específico por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ================================
    // CREAR USUARIO
    // ================================
    @Operation(
            summary = "Crear usuario",
            description = "Registra un nuevo usuario en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuario ya existe")
    })
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario user) {
        Usuario newUser = service.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // ================================
    // ACTUALIZAR USUARIO
    // ================================
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(
            @PathVariable Long id,
            @RequestBody Usuario user
    ) {
        return ResponseEntity.ok(service.update(id, user));
    }

    // ================================
    // ELIMINAR USUARIO
    // ================================
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ================================
    // LOGIN
    // ================================
    @Operation(
            summary = "Login de usuario",
            description = "Autentica un usuario por email y contraseña."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        try {
            Usuario user = service.login(email, password);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // ================================
    // CAMBIAR ROL
    // ================================
    @Operation(
            summary = "Cambiar rol de usuario",
            description = "Cambia el rol de un usuario (ej: CLIENTE → ADMIN)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
    })
    @PutMapping("/{idUsuario}/rol/{idRol}")
    public ResponseEntity<Usuario> cambiarRol(
            @PathVariable Long idUsuario,
            @PathVariable Long idRol
    ) {
        return ResponseEntity.ok(service.cambiarRol(idUsuario, idRol));
    }

    // ================================
    // CAMBIAR PASSWORD
    // ================================
    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite al usuario cambiar su contraseña actual por una nueva."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contraseña cambiada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Contraseña actual incorrecta"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String passwordActual = body.get("passwordActual");
        String passwordNueva = body.get("passwordNueva");

        service.cambiarPassword(id, passwordActual, passwordNueva);
        return ResponseEntity.noContent().build();
    }
}
