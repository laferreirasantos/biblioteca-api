package com.pge.biblioteca.domain.service;

import com.pge.biblioteca.domain.model.Emprestimo;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.domain.model.Usuario;
import com.pge.biblioteca.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoServiceTest {

    private EmprestimoService emprestimoService;

    @BeforeEach
    void setUp() {
        emprestimoService = new EmprestimoService(null);
    }

    @Test
    void deveRealizarEmprestimoComSucesso() {
        Usuario usuario = new Usuario("Maria", "123", "maria@pge.gov.br");
        Livro livro1 = new Livro("Livro 1", "Autor", "111", 2);
        Livro livro2 = new Livro("Livro 2", "Autor", "222", 1);

        List<Livro> livros = List.of(livro1, livro2);

        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(usuario, livros, LocalDate.of(2025, 6, 10));

        assertNotNull(emprestimo);
        assertEquals(2, emprestimo.getLivros().size());
        assertEquals(LocalDate.of(2025, 6, 10), emprestimo.getDataInicio());
        assertEquals(LocalDate.of(2025, 6, 24), emprestimo.getDataPrevistaDevolucao());
        assertEquals(1, livro1.getQuantidadeDisponivel());
        assertEquals(0, livro2.getQuantidadeDisponivel());
    }

    @Test
    void deveLancarExcecaoSeLivroSemEstoque() {
        Usuario usuario = new Usuario("João", "456", "joao@pge.gov.br");
        Livro livro = new Livro("Livro X", "Autor", "999", 0);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            emprestimoService.realizarEmprestimo(usuario, List.of(livro), LocalDate.now());
        });

        assertEquals("Livro sem unidades disponíveis", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeUsuarioTemMaisDeCincoEmprestimos() {
        Usuario usuario = new Usuario("Carlos", "789", "carlos@pge.gov.br");

        // Simulando 5 empréstimos ativos
        for (int i = 1; i <= 5; i++) {
            usuario.adicionarEmprestimo(new Emprestimo());
        }

        Livro livro = new Livro("Livro Y", "Autor", "888", 3);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            emprestimoService.realizarEmprestimo(usuario, List.of(livro), LocalDate.now());
        });

        assertEquals("Usuário excedeu o limite de 5 empréstimos simultâneos", exception.getMessage());
    }
}
