package com.pge.biblioteca.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pge.biblioteca.api.dto.LivroRequest;
import com.pge.biblioteca.api.dto.LivroResponse;
import com.pge.biblioteca.domain.service.LivroService;
import com.pge.biblioteca.application.LivroApplicationService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    @MockBean
    private LivroApplicationService livroApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCadastrarLivroComSucesso() throws Exception {
        LivroRequest request = new LivroRequest("Clean Code", "Robert Martin", "123456789", 10);
        LivroResponse response = new LivroResponse(1L, "Clean Code", "Robert Martin", "123456789", 10);

        Mockito.when(livroService.cadastrar(any(LivroRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.titulo").value("Clean Code"))
            .andExpect(jsonPath("$.isbn").value("123456789"));
    }

    @Test
    void deveListarLivrosComSucesso() throws Exception {
        List<LivroResponse> lista = List.of(
                new LivroResponse(1L, "Clean Code", "Robert Martin", "123456789", 10),
                new LivroResponse(2L, "Effective Java", "Joshua Bloch", "987654321", 5)
        );

        Mockito.when(livroService.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/api/livros"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].titulo").value("Clean Code"))
            .andExpect(jsonPath("$[1].titulo").value("Effective Java"));
    }

    @Test
    void deveListarApenasLivrosDisponiveis() throws Exception {
        List<LivroResponse> disponiveis = List.of(
                new LivroResponse(1L, "Clean Code", "Robert Martin", "123456789", 10)
        );

        Mockito.when(livroApplicationService.listarLivrosDisponiveis()).thenReturn(disponiveis);

        mockMvc.perform(get("/api/livros/disponiveis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].titulo").value("Clean Code"));
    }
}
