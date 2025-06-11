package com.pge.biblioteca.api.dto;

import java.time.LocalDate;
import java.util.List;

public record EmprestimoResponse(
	    Long id,
	    Long usuarioId,
	    List<Long> livrosIds,
	    LocalDate dataInicio,
	    LocalDate dataPrevistaDevolucao,
	    LocalDate dataDevolucao
	) {}

