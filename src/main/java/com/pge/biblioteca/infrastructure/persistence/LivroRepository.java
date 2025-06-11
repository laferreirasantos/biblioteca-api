package com.pge.biblioteca.infrastructure.persistence;

import com.pge.biblioteca.domain.model.Livro;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
    
    List<Livro> findByQuantidadeDisponivelGreaterThan(int quantidade);

}
