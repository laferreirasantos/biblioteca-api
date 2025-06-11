package com.pge.biblioteca.api.controller;

import com.pge.biblioteca.api.dto.LivroRequest;
import com.pge.biblioteca.api.dto.LivroResponse;
import com.pge.biblioteca.application.LivroApplicationService;
import com.pge.biblioteca.domain.service.LivroService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;
    
    private final LivroApplicationService livroApplicationService;


    public LivroController(LivroService livroService, LivroApplicationService livroApplicationService) {
        this.livroService = livroService;
        this.livroApplicationService = livroApplicationService;
    }

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(@RequestBody @Valid LivroRequest request) {
        LivroResponse response = livroService.cadastrar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponse>> listar() {
        return ResponseEntity.ok(livroService.listarTodos());
    }
    
    @GetMapping("/disponiveis")
    public ResponseEntity<List<LivroResponse>> listarDisponiveis() {
        return ResponseEntity.ok(livroApplicationService.listarLivrosDisponiveis());
    }

    

}
