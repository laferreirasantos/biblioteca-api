package com.pge.biblioteca;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode("123456"); 
        System.out.println("Senha criptografada: " + senhaCriptografada);
    }
}
