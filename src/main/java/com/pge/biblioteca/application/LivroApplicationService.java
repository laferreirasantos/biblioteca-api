package com.pge.biblioteca.application;

import com.pge.biblioteca.api.dto.LivroResponse;
import com.pge.biblioteca.infrastructure.persistence.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroApplicationService {

    private final LivroRepository livroRepository;

    public LivroApplicationService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<LivroResponse> listarLivrosDisponiveis() {
        return livroRepository.findByQuantidadeDisponivelGreaterThan(0)
                .stream()
                .map(l -> new LivroResponse(
                        l.getId(),
                        l.getTitulo(),
                        l.getAutor(),
                        l.getIsbn(),
                        l.getQuantidadeDisponivel()
                ))
                .toList();
    }
}
