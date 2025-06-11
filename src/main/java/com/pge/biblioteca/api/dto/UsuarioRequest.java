package com.pge.biblioteca.api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UsuarioRequest(
	@NotBlank(message = "Nome é obrigatório")
	String nome,
	@NotBlank(message = "Matrícula é obrigatório")
    String matricula,
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    String emaill
) {}
