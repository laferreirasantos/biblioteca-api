package com.pge.biblioteca.domain.service;

import com.pge.biblioteca.api.dto.LivroRequest;
import com.pge.biblioteca.api.dto.LivroResponse;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.infrastructure.persistence.LivroRepository;
import com.pge.biblioteca.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivroServiceTest {

    private LivroRepository livroRepository;
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroRepository = mock(LivroRepository.class);
        livroService = new LivroService(livroRepository);
    }

    @Test
    void deveCadastrarLivroComSucesso() {
        // Arrange
        LivroRequest request = new LivroRequest("Clean Code", "Robert Martin", "123456789", 10);

        when(livroRepository.existsByIsbn("123456789")).thenReturn(false);

        Livro livroSalvo = new Livro("Clean Code", "Robert Martin", "123456789", 10);
        livroSalvo.setId(1L);
        when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

        // Act
        LivroResponse response = livroService.cadastrar(request);

        // Assert
        assertNotNull(response);
        assertEquals("Clean Code", response.titulo());
        assertEquals("123456789", response.isbn());
        assertEquals(10, response.quantidadeDisponivel());

        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void naoDeveCadastrarLivroComIsbnDuplicado() {
        // Arrange
        LivroRequest request = new LivroRequest("Clean Code", "Robert Martin", "123456789", 10);
        when(livroRepository.existsByIsbn("123456789")).thenReturn(true);

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> livroService.cadastrar(request));
        assertEquals("ISBN já cadastrado", ex.getMessage());

        verify(livroRepository, never()).save(any());
    }

    @Test
    void deveListarTodosOsLivros() {
        // Arrange
        Livro livro1 = new Livro("Clean Code", "Robert Martin", "111", 10);
        livro1.setId(1L);
        Livro livro2 = new Livro("Effective Java", "Joshua Bloch", "222", 5);
        livro2.setId(2L);

        when(livroRepository.findAll()).thenReturn(List.of(livro1, livro2));

        // Act
        List<LivroResponse> livros = livroService.listarTodos();

        // Assert
        assertEquals(2, livros.size());
        assertEquals("Clean Code", livros.get(0).titulo());
        assertEquals("Effective Java", livros.get(1).titulo());
    }
}
