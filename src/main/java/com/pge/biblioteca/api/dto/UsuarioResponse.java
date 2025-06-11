package com.pge.biblioteca.api.dto;



public record UsuarioResponse(
    Long id,  
	String nome,
    String matricula,
    String email
) {}
