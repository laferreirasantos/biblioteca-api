package com.pge.biblioteca.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pge.biblioteca.api.dto.EmprestimoRequest;
import com.pge.biblioteca.api.dto.EmprestimoResponse;
import com.pge.biblioteca.application.EmprestimoApplicationService;
import com.pge.biblioteca.domain.model.Emprestimo;
import com.pge.biblioteca.domain.model.Livro;
import com.pge.biblioteca.domain.model.Usuario;
import com.pge.biblioteca.domain.service.EmprestimoService;
import com.pge.biblioteca.infrastructure.persistence.EmprestimoRepository;
import com.pge.biblioteca.infrastructure.persistence.LivroRepository;
import com.pge.biblioteca.infrastructure.persistence.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmprestimoController.class)
class EmprestimoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmprestimoApplicationService emprestimoApplicationService;

    @MockBean
    private EmprestimoRepository emprestimoRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private LivroRepository livroRepository;

    @MockBean
    private EmprestimoService emprestimoService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void deveRealizarEmprestimoComSucesso() throws Exception {
        Long usuarioId = 1L;
        List<Long> livrosIds = List.of(10L, 20L);
        LocalDate dataInicio = LocalDate.of(2025, 6, 10);
        LocalDate dataPrevistaDevolucao = dataInicio.plusDays(14);

        Usuario usuario = new Usuario("Joana", "123", "joana@pge.gov.br");
        usuario.setId(usuarioId);

        Livro livro1 = new Livro("Livro 1", "Autor 1", "111", 5);
        livro1.setId(10L);
        Livro livro2 = new Livro("Livro 2", "Autor 2", "222", 2);
        livro2.setId(20L);

        Emprestimo emprestimo = new Emprestimo(usuario, List.of(livro1, livro2), dataInicio, dataPrevistaDevolucao);
        emprestimo.setId(100L);

        Mockito.when(emprestimoApplicationService.realizarEmprestimo(eq(usuarioId), eq(livrosIds)))
            .thenReturn(emprestimo);

        EmprestimoRequest request = new EmprestimoRequest(usuarioId, livrosIds);

        mockMvc.perform(post("/api/emprestimos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.usuarioId").value(usuarioId))
            .andExpect(jsonPath("$.livrosIds.length()").value(2))
            .andExpect(jsonPath("$.dataInicio").value(dataInicio.toString()))
            .andExpect(jsonPath("$.dataPrevistaDevolucao").value(dataPrevistaDevolucao.toString()));
    }
}
