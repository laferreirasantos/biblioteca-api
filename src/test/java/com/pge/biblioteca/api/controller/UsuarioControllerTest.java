package com.pge.biblioteca.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pge.biblioteca.api.dto.UsuarioRequest;
import com.pge.biblioteca.api.dto.UsuarioResponse;
import com.pge.biblioteca.domain.service.UsuarioService;

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

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCadastrarUsuarioComSucesso() throws Exception {
        UsuarioRequest request = new UsuarioRequest("Larissa", "123", "larissa@pge.gov.br");

        UsuarioResponse response = new UsuarioResponse(1L, "Larissa", "123", "larissa@pge.gov.br");

        Mockito.when(usuarioService.cadastrar(any(UsuarioRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.nome").value("Larissa"))
            .andExpect(jsonPath("$.matricula").value("123"))
            .andExpect(jsonPath("$.email").value("larissa@pge.gov.br"));
    }

    @Test
    void deveListarUsuariosComSucesso() throws Exception {
        List<UsuarioResponse> usuarios = List.of(
            new UsuarioResponse(1L, "Larissa", "123", "larissa@pge.gov.br"),
            new UsuarioResponse(2L, "João", "456", "joao@pge.gov.br")
        );

        Mockito.when(usuarioService.listarTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].nome").value("Larissa"))
            .andExpect(jsonPath("$[1].nome").value("João"));
    }
}
