package com.pge.biblioteca.api.controller;

import com.pge.biblioteca.api.dto.LoginRequest;
import com.pge.biblioteca.api.dto.TokenResponse;
import com.pge.biblioteca.security.jwt.JwtService;
import com.pge.biblioteca.security.model.UsuarioSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getMatricula(),
                        request.getSenha()
                )
        );

        UsuarioSecurity usuarioSecurity = (UsuarioSecurity) authentication.getPrincipal();
        String token = jwtService.gerarToken(usuarioSecurity);

        return new TokenResponse(token);
    }
}
