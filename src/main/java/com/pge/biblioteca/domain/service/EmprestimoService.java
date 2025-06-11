package com.pge.biblioteca.domain.service;

import com.pge.biblioteca.domain.factory.LoanFactory;
import com.pge.biblioteca.domain.model.Emprestimo;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.domain.model.Usuario;
import com.pge.biblioteca.infrastructure.persistence.EmprestimoRepository;
import com.pge.biblioteca.shared.exception.BusinessException;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }


	public Emprestimo realizarEmprestimo(Usuario usuario, List<Livro> livros, LocalDate dataInicio) {
        validarDisponibilidadeDosLivros(livros);
        validarLimiteDeEmprestimos(usuario);

        Emprestimo emprestimo = LoanFactory.criarEmprestimo(usuario, livros, dataInicio);

        livros.forEach(Livro::reduzirQuantidadeDisponivel);

        return emprestimo;
    }


    public void validarDisponibilidadeDoLivros(List<Livro> livros) {
        for (Livro livro : livros) {
            if (!livro.possuiDisponibilidade()) {
                throw new BusinessException("Livro sem unidades disponíveis");
            }
        }
    }

    private void validarLimiteDeEmprestimos(Usuario usuario) {
        List<Emprestimo> ativos = emprestimoRepository.findByUsuarioAndDataDevolucaoIsNull(usuario);
        if (ativos.size() >= 5) {
            throw new BusinessException("Usuário já atingiu o limite de 5 empréstimos.");
        }
    }
    
    private void validarDisponibilidadeDosLivros(List<Livro> livros) {
        boolean algumIndisponivel = livros.stream()
            .anyMatch(livro -> !livro.possuiDisponibilidade());

        if (algumIndisponivel) {
            throw new BusinessException("Um ou mais livros não estão disponíveis para empréstimo.");
        }
    }

}
