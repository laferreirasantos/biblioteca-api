package com.pge.biblioteca.api.dto;


public record LivroResponse(
    Long id,
    String titulo,
    String autor,
    String isbn,
    Integer quantidadeDisponivel
) {}
