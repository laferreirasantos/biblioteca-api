package com.pge.biblioteca.api.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;



public record EmprestimoRequest(
	    @NotNull(message = "ID do usuário é obrigatório")
	    Long usuarioId,

	    @NotEmpty(message = "Lista de livros não pode estar vazia")
	    List<Long> livrosIds
	) {}

