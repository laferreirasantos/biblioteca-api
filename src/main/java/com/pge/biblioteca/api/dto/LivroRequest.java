package com.pge.biblioteca.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record LivroRequest(
	@NotBlank(message = "Título é obrigatório")
	String titulo,
	@NotBlank(message = "Autor é obrigatório")
    String autor,
    @NotBlank(message = "ISBN é obrigatório") 
    String isbn,
    @Min(value = 0, message = "A quantidade deve ser maior ou igual a zero")
    Integer quantidadeDisponivel
) {}
