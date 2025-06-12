package com.pge.biblioteca.security.service;

import com.pge.biblioteca.domain.model.Usuario;
import com.pge.biblioteca.infrastructure.persistence.UsuarioRepository;
import com.pge.biblioteca.security.model.UsuarioSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioSecurityService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioSecurityService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com a matrícula: " + matricula));

        return new UsuarioSecurity(usuario);
    }
}
