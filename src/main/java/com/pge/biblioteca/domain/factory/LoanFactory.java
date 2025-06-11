package com.pge.biblioteca.domain.factory;

import com.pge.biblioteca.domain.model.Emprestimo;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.domain.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public class LoanFactory {

    private static final int MAX_DIAS_DEVOLUCAO = 14;

    public static Emprestimo criarEmprestimo(Usuario usuario, List<Livro> livros, LocalDate dataInicio) {
        LocalDate dataPrevistaDevolucao = dataInicio.plusDays(MAX_DIAS_DEVOLUCAO);

        return new Emprestimo(usuario, livros, dataInicio, dataPrevistaDevolucao);
    }
}
