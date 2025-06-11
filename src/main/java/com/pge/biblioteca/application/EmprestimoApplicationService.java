package com.pge.biblioteca.application;

import com.pge.biblioteca.domain.model.Emprestimo;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.domain.model.Usuario;
import com.pge.biblioteca.domain.service.EmprestimoService;
import com.pge.biblioteca.infrastructure.persistence.EmprestimoRepository;
import com.pge.biblioteca.infrastructure.persistence.LivroRepository;
import com.pge.biblioteca.infrastructure.persistence.UsuarioRepository;
import com.pge.biblioteca.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoApplicationService {

    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final EmprestimoService emprestimoDomainService;

    public EmprestimoApplicationService(
        LivroRepository livroRepository,
        UsuarioRepository usuarioRepository,
        EmprestimoRepository emprestimoRepository,
        EmprestimoService emprestimoDomainService
    ) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.emprestimoDomainService = emprestimoDomainService;
    }

    @Transactional
    public Emprestimo realizarEmprestimo(Long usuarioId, List<Long> livrosIds) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        List<Livro> livros = livroRepository.findAllById(livrosIds);
        
        if (livros.size() != livrosIds.size()) {
            throw new BusinessException("Um ou mais livros não foram encontrados.");
        }

        Emprestimo emprestimo = emprestimoDomainService.realizarEmprestimo(usuario, livros, LocalDate.now());

        // Atualiza quantidade de cada livro
        livros.forEach(livroRepository::save);
        
        return emprestimoRepository.save(emprestimo);
    }
    
    @Transactional
    public void registrarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
            .orElseThrow(() -> new BusinessException("Empréstimo não encontrado"));

        if (emprestimo.isDevolvido()) {
            throw new BusinessException("Este empréstimo já foi devolvido");
        }

        // Atualiza a data de devolução e quantidade dos livros
        emprestimo.registrarDevolucao(LocalDate.now());

        emprestimo.getLivros().forEach(livro -> {
            livroRepository.save(livro); // Persiste a quantidade atualizada
        });

        emprestimoRepository.save(emprestimo);
    }

    public boolean usuarioPossuiEmprestimosAtrasados(Long usuarioId) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuarioId(usuarioId);

        return emprestimos.stream()
                .anyMatch(e -> e.getDataDevolucao() == null && 
                               e.getDataPrevistaDevolucao().isBefore(LocalDate.now()));
    }


}
