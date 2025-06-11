package com.pge.biblioteca.api.controller;

import com.pge.biblioteca.api.dto.EmprestimoRequest;
import com.pge.biblioteca.api.dto.EmprestimoResponse;
import com.pge.biblioteca.application.EmprestimoApplicationService;
import com.pge.biblioteca.domain.model.Emprestimo;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.infrastructure.persistence.EmprestimoRepository;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoApplicationService emprestimoService;
    
    private final EmprestimoRepository emprestimoRepository;

    public EmprestimoController(EmprestimoApplicationService emprestimoService, EmprestimoRepository emprestimoRepository) {
        this.emprestimoService = emprestimoService;
        this.emprestimoRepository = emprestimoRepository;
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponse> realizarEmprestimo(@RequestBody @Valid EmprestimoRequest request) {
        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(
            request.usuarioId(),
            request.livrosIds()
        );

        List<Long> livrosIds = emprestimo.getLivros()
            .stream()
            .map(Livro::getId)
            .toList();

        EmprestimoResponse response = new EmprestimoResponse(
            emprestimo.getId(),
            emprestimo.getUsuario().getId(),
            livrosIds,
            emprestimo.getDataInicio(),
            emprestimo.getDataPrevistaDevolucao(),
            emprestimo.getDataDevolucao()
        );

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/devolucao")
    public ResponseEntity<Void> registrarDevolucao(@PathVariable Long id) {
        emprestimoService.registrarDevolucao(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EmprestimoResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<Emprestimo> emprestimos = emprestimoRepository.findByUsuarioId(usuarioId);

        List<EmprestimoResponse> response = emprestimos.stream()
            .map(emprestimo -> new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getUsuario().getId(),
                emprestimo.getLivros().stream().map(Livro::getId).toList(),
                emprestimo.getDataInicio(),
                emprestimo.getDataPrevistaDevolucao(),
                emprestimo.getDataDevolucao()
            ))
            .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}/atraso")
    public ResponseEntity<Boolean> verificarAtrasos(@PathVariable Long usuarioId) {
        boolean possuiAtraso = emprestimoService.usuarioPossuiEmprestimosAtrasados(usuarioId);
        return ResponseEntity.ok(possuiAtraso);
    }


}
