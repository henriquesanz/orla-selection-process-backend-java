package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.ProjetoRequest;
import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.domain.Projeto;
import com.corelyon.mvp.domain.repository.FuncionarioRepository;
import com.corelyon.mvp.domain.repository.ProjetoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarProjetoUseCaseTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private CriarProjetoUseCase criarProjetoUseCase;

    private ProjetoRequest projetoRequest;

    @BeforeEach
    void setUp() {
        projetoRequest = new ProjetoRequest(
            "Projeto Teste",
            "Descrição do projeto teste",
            null
        );
    }

    @Test
    void deveCriarProjetoComSucesso() {
        // Arrange
        when(projetoRepository.existePorNome(anyString())).thenReturn(false);
        when(projetoRepository.salvar(any(Projeto.class))).thenReturn(
            new Projeto(1L, "Projeto Teste", "Descrição do projeto teste", LocalDateTime.now())
        );

        // Act
        ProjetoResponse response = criarProjetoUseCase.executar(projetoRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Projeto Teste", response.nome());
        assertEquals("Descrição do projeto teste", response.descricao());
        assertNotNull(response.dataCriacao());
        
        verify(projetoRepository).existePorNome("Projeto Teste");
        verify(projetoRepository).salvar(any(Projeto.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExiste() {
        // Arrange
        when(projetoRepository.existePorNome(anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> criarProjetoUseCase.executar(projetoRequest));
        
        assertEquals("Projeto com nome Projeto Teste já existe", exception.getMessage());
        verify(projetoRepository).existePorNome("Projeto Teste");
        verify(projetoRepository, never()).salvar(any(Projeto.class));
    }
}
