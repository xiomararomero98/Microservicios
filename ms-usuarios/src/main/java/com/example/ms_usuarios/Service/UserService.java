package com.example.ms_usuarios.Service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.ms_usuarios.Model.Rol;
import com.example.ms_usuarios.Model.Usuario;
import com.example.ms_usuarios.Repository.RolRepository;
import com.example.ms_usuarios.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final RolRepository rolRepository;

    public UserService(UserRepository repository, RolRepository rolRepository) {
        this.repository = repository;
        this.rolRepository = rolRepository;
    }

    // ==========================================================
    // LISTAR TODOS
    // ==========================================================
    public List<Usuario> getAll() {
        return repository.findAll();
    }

    // ==========================================================
    // BUSCAR POR ID
    // ==========================================================
    public Usuario getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // ==========================================================
    // VALIDACIONES
    // ==========================================================
    private void validarDatosBase(Usuario user) {

        if (user.getNombre() == null || user.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        if (user.getApellido() == null || user.getApellido().isBlank()) {
            throw new RuntimeException("El apellido es obligatorio");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }

        if (!user.getEmail().contains("@")) {
            throw new RuntimeException("El email no es válido");
        }

        if (user.getTelefono() == null || user.getTelefono().isBlank()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }

        if (user.getDireccion() == null || user.getDireccion().isBlank()) {
            throw new RuntimeException("La dirección es obligatoria");
        }
    }

    // Create: password obligatoria
    private void validarCreate(Usuario user) {
        validarDatosBase(user);

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        if (user.getPassword().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }
    }

    // Update: password opcional (solo valida si viene)
    private void validarUpdate(Usuario user) {
        validarDatosBase(user);

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            if (user.getPassword().length() < 6) {
                throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
            }
        }
    }

    // ==========================================================
    // ROL: traer el rol REAL desde BD (evita rol.nombre = null)
    // ==========================================================
    private Rol resolverRolDesdeRequest(Usuario user) {
        if (user.getRol() == null || user.getRol().getId() == null) return null;

        Long idRol = user.getRol().getId();
        return rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + idRol));
    }

    private Rol rolDefaultCliente() {
        return rolRepository.findByNombreIgnoreCase("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no existe en BD"));
    }

    // ==========================================================
    // CREAR USUARIO
    // ==========================================================
    public Usuario create(Usuario user) {

        validarCreate(user);

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // ✅ Rol: si vino {rol:{id:2}} lo traemos real desde BD
        Rol rolReal = resolverRolDesdeRequest(user);
        if (rolReal != null) {
            user.setRol(rolReal);
        } else {
            user.setRol(rolDefaultCliente());
        }

        // ✅ Encriptar contraseña
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);

        return repository.save(user);
    }

    // ==========================================================
    // ACTUALIZAR USUARIO
    // ==========================================================
    public Usuario update(Long id, Usuario user) {

        Usuario dbUser = getById(id);

        validarUpdate(user);

        // ✅ Email duplicado si lo cambió
        if (!dbUser.getEmail().equalsIgnoreCase(user.getEmail())
                && repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        dbUser.setNombre(user.getNombre());
        dbUser.setApellido(user.getApellido());
        dbUser.setEmail(user.getEmail());
        dbUser.setTelefono(user.getTelefono());
        dbUser.setDireccion(user.getDireccion());

        // ✅ Password opcional: si viene, se encripta; si no, se mantiene
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            dbUser.setPassword(hashed);
        }

        // ✅ Rol opcional: si viene, buscar rol real por ID
        Rol rolReal = resolverRolDesdeRequest(user);
        if (rolReal != null) {
            dbUser.setRol(rolReal);
        }

        return repository.save(dbUser);
    }

    // ==========================================================
    // ELIMINAR USUARIO
    // ==========================================================
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No existe un usuario con id: " + id);
        }
        repository.deleteById(id);
    }

    // ==========================================================
    // LOGIN
    // ==========================================================
    public Usuario login(String email, String password) {

        Usuario user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Correo no registrado"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return user;
    }

    // ==========================================================
    // CAMBIAR ROL
    // ==========================================================
    public Usuario cambiarRol(Long idUsuario, Long idRol) {

        Usuario usuario = repository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));

        Rol nuevoRol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + idRol));

        usuario.setRol(nuevoRol);

        return repository.save(usuario);
    }

    
    // ==========================================================
    // CAMBIAR contraseña
    // ==========================================================

public void cambiarPassword(Long id, String passwordActual, String passwordNueva) {

    Usuario user = getById(id);

    if (!BCrypt.checkpw(passwordActual, user.getPassword())) {
        throw new RuntimeException("Contraseña actual incorrecta");
    }

    if (passwordNueva == null || passwordNueva.length() < 6) {
        throw new RuntimeException("La nueva contraseña debe tener al menos 6 caracteres");
    }

    String hashed = BCrypt.hashpw(passwordNueva, BCrypt.gensalt());
    user.setPassword(hashed);

    repository.save(user);
}
}
