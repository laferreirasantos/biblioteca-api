package com.pge.biblioteca.domain.service;

import com.pge.biblioteca.domain.factory.LoanFactory;
import com.pge.biblioteca.domain.model.Emprestimo;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.domain.model.Usuario;
import com.pge.biblioteca.infrastructure.persistence.EmprestimoRepository;
import com.pge.biblioteca.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmprestimoServiceTest {

    private EmprestimoService emprestimoService;
    private EmprestimoRepository emprestimoRepository;

    @BeforeEach
    void setUp() {
        emprestimoRepository = Mockito.mock(EmprestimoRepository.class);
        emprestimoService = new EmprestimoService(emprestimoRepository);
    }

    @Test
    void deveRealizarEmprestimoComSucesso() {
        Usuario usuario = new Usuario("Maria", "123", "maria@pge.gov.br");
        Livro livro1 = new Livro("Livro 1", "Autor", "111", 2);
        Livro livro2 = new Livro("Livro 2", "Autor", "222", 1);
        List<Livro> livros = List.of(livro1, livro2);

        when(emprestimoRepository.findByUsuarioAndDataDevolucaoIsNull(usuario))
            .thenReturn(Collections.emptyList());

        LocalDate dataInicio = LocalDate.of(2025, 6, 10);
        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(usuario, livros, dataInicio);

        assertNotNull(emprestimo);
        assertEquals(2, emprestimo.getLivros().size());
        assertEquals(dataInicio, emprestimo.getDataInicio());
        assertEquals(dataInicio.plusDays(14), emprestimo.getDataPrevistaDevolucao());
        assertEquals(1, livro1.getQuantidadeDisponivel());
        assertEquals(0, livro2.getQuantidadeDisponivel());
    }

    @Test
    void deveLancarExcecaoSeLivroSemEstoque() {
        Usuario usuario = new Usuario("João", "456", "joao@pge.gov.br");
        Livro livro = new Livro("Livro X", "Autor", "999", 0);

        when(emprestimoRepository.findByUsuarioAndDataDevolucaoIsNull(usuario))
            .thenReturn(Collections.emptyList());

        BusinessException exception = assertThrows(BusinessException.class, () ->
            emprestimoService.realizarEmprestimo(usuario, List.of(livro), LocalDate.now())
        );

        assertEquals("Um ou mais livros não estão disponíveis para empréstimo.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeUsuarioTemMaisDeCincoEmprestimos() {
        Usuario usuario = new Usuario("Carlos", "789", "carlos@pge.gov.br");

        List<Emprestimo> emprestimosAtivos = List.of(
                new Emprestimo(), new Emprestimo(), new Emprestimo(),
                new Emprestimo(), new Emprestimo()
        );

        when(emprestimoRepository.findByUsuarioAndDataDevolucaoIsNull(usuario))
            .thenReturn(emprestimosAtivos);

        Livro livro = new Livro("Livro Y", "Autor", "888", 3);

        BusinessException exception = assertThrows(BusinessException.class, () ->
            emprestimoService.realizarEmprestimo(usuario, List.of(livro), LocalDate.now())
        );

        assertEquals("Usuário já atingiu o limite de 5 empréstimos.", exception.getMessage());
    }
}
