package com.example.ms_usuarios.Service;

import org.mindrot.jbcrypt.BCrypt;

public class Encriptador {

    // Encriptar contraseña
    public static String encriptar(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Comparar contraseña ingresada vs encriptada
    public static boolean verificar(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
