package com.pge.biblioteca.domain.service;

import com.pge.biblioteca.api.dto.UsuarioRequest;
import com.pge.biblioteca.api.dto.UsuarioResponse;
import com.pge.biblioteca.domain.model.Usuario;
import com.pge.biblioteca.infrastructure.persistence.UsuarioRepository;
import com.pge.biblioteca.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        // Arrange
        UsuarioRequest request = new UsuarioRequest("Larissa", "2023001", "larissa@pge.gov.br");
        when(usuarioRepository.existsByMatricula("2023001")).thenReturn(false);

        Usuario salvo = new Usuario("Larissa", "2023001", "larissa@pge.gov.br");
        salvo.setId(1L);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(salvo);

        // Act
        UsuarioResponse response = usuarioService.cadastrar(request);

        // Assert
        assertNotNull(response);
        assertEquals("Larissa", response.nome());
        assertEquals("2023001", response.matricula());
        assertEquals("larissa@pge.gov.br", response.email());

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void naoDeveCadastrarUsuarioComMatriculaDuplicada() {
        UsuarioRequest request = new UsuarioRequest("Carlos", "2023002", "carlos@pge.gov.br");
        when(usuarioRepository.existsByMatricula("2023002")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            usuarioService.cadastrar(request);
        });

        assertEquals("Matrícula já cadastrada", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailInvalido() {
        UsuarioRequest request = new UsuarioRequest("Joana", "2023003", "joana-email.com");
        when(usuarioRepository.existsByMatricula("2023003")).thenReturn(false);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            usuarioService.cadastrar(request);
        });

        assertEquals("Formato de email inválido", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveListarTodosUsuarios() {
        Usuario usuario1 = new Usuario("Ana", "123", "ana@pge.gov.br");
        usuario1.setId(1L);
        Usuario usuario2 = new Usuario("João", "456", "joao@pge.gov.br");
        usuario2.setId(2L);

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        List<UsuarioResponse> result = usuarioService.listarTodos();

        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).nome());
        assertEquals("João", result.get(1).nome());
    }
}
