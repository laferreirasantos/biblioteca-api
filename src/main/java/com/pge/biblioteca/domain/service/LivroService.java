package com.pge.biblioteca.domain.service;

import com.pge.biblioteca.api.dto.LivroRequest;
import com.pge.biblioteca.api.dto.LivroResponse;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.infrastructure.persistence.LivroRepository;
import com.pge.biblioteca.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Transactional
    public LivroResponse cadastrar(LivroRequest request) {
        if (livroRepository.existsByIsbn(request.isbn())) {
            throw new BusinessException("ISBN já cadastrado");
        }

        Livro livro = new Livro(
            request.titulo(),
            request.autor(),
            request.isbn(),
            request.quantidadeDisponivel()
        );

        Livro salvo = livroRepository.save(livro);
        return toResponse(salvo);
    }

    public List<LivroResponse> listarTodos() {
        return livroRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LivroResponse toResponse(Livro livro) {
        return new LivroResponse(
            livro.getId(),
            livro.getTitulo(),
            livro.getAutor(),
            livro.getIsbn(),
            livro.getQuantidadeDisponivel()
        );
    }
}
