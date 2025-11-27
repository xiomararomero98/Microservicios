package com.example.ms_usuarios.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_usuarios.Model.Usuario;
import com.example.ms_usuarios.Model.Rol;
import com.example.ms_usuarios.Repository.UserRepository;
import com.example.ms_usuarios.Repository.RolRepository;

import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

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
    // VALIDACIÓN GENERAL PARA CREAR Y ACTUALIZAR
    // ==========================================================
    private void validarUsuario(Usuario user) {

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

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }
    }

    // ==========================================================
    // CREAR USUARIO
    // ==========================================================
    public Usuario create(Usuario user) {

        validarUsuario(user); // Validaciones completas

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        // encriptar contraseña
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);

        return repository.save(user);
    }

    // ==========================================================
    // ACTUALIZAR USUARIO
    // ==========================================================
    public Usuario update(Long id, Usuario user) {

        Usuario dbUser = getById(id); // si no existe lanza error

        validarUsuario(user); // Validar antes de actualizar

        dbUser.setNombre(user.getNombre());
        dbUser.setApellido(user.getApellido());
        dbUser.setEmail(user.getEmail());
        dbUser.setPassword(user.getPassword());
        dbUser.setTelefono(user.getTelefono());
        dbUser.setDireccion(user.getDireccion());

          // Encriptar si viene una nueva contraseña
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            dbUser.setPassword(hashed);
        }

        // si viene rol lo actualiza
        if (user.getRol() != null) {
            dbUser.setRol(user.getRol());
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

         // Comparar contraseña con BCrypt
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

}
