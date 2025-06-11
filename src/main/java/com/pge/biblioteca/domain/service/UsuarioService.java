package com.pge.biblioteca.domain.service;

import com.pge.biblioteca.api.dto.UsuarioRequest;
import com.pge.biblioteca.api.dto.UsuarioResponse;
import com.pge.biblioteca.domain.model.Usuario;
import com.pge.biblioteca.infrastructure.persistence.UsuarioRepository;
import com.pge.biblioteca.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponse cadastrar(UsuarioRequest request) {
        if (usuarioRepository.existsByMatricula(request.matricula())) {
            throw new BusinessException("Matrícula já cadastrada");
        }

        if (!request.emaill().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new BusinessException("Formato de email inválido");
        }

        Usuario usuario = new Usuario(
                request.nome(),
                request.matricula(),
                request.emaill()
        );

        Usuario salvo = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getMatricula(),
                salvo.getEmail()
        );
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponse(
                        u.getId(),
                        u.getNome(),
                        u.getMatricula(),
                        u.getEmail()
                ))
                .toList();
    }
}
