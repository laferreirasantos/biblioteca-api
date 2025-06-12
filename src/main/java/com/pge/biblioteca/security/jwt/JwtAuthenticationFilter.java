package com.pge.biblioteca.security.jwt;

import com.pge.biblioteca.security.model.UsuarioSecurity;
import com.pge.biblioteca.security.service.UsuarioSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioSecurityService usuarioSecurityService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UsuarioSecurityService usuarioSecurityService) {
        this.jwtUtil = jwtUtil;
        this.usuarioSecurityService = usuarioSecurityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String matricula = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            matricula = jwtUtil.extrairMatricula(token);
        }

        if (matricula != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsuarioSecurity usuario = (UsuarioSecurity) usuarioSecurityService.loadUserByUsername(matricula);

            if (jwtUtil.validarToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
