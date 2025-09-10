package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.ProjetoRequest;
import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.infra.entity.ProjetoEntity;
import com.corelyon.mvp.infra.repository.FuncionarioRepositoryJpa;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
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
    private ProjetoRepositoryJpa projetoRepositoryJpa;

    @Mock
    private FuncionarioRepositoryJpa funcionarioRepositoryJpa;

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
        when(projetoRepositoryJpa.existsByNome(anyString())).thenReturn(false);
        ProjetoEntity projetoSalvo = new ProjetoEntity("Projeto Teste", "Descrição do projeto teste", LocalDateTime.now());
        projetoSalvo.setId(1L);
        when(projetoRepositoryJpa.save(any(ProjetoEntity.class))).thenReturn(projetoSalvo);

        ProjetoResponse response = criarProjetoUseCase.executar(projetoRequest);

        assertNotNull(response);
        assertEquals("Projeto Teste", response.nome());
        assertEquals("Descrição do projeto teste", response.descricao());
        assertNotNull(response.dataCriacao());
        
        verify(projetoRepositoryJpa).existsByNome("Projeto Teste");
        verify(projetoRepositoryJpa).save(any(ProjetoEntity.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExiste() {
        when(projetoRepositoryJpa.existsByNome(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> criarProjetoUseCase.executar(projetoRequest));
        
        assertEquals("Projeto com nome Projeto Teste já existe", exception.getMessage());
        verify(projetoRepositoryJpa).existsByNome("Projeto Teste");
        verify(projetoRepositoryJpa, never()).save(any(ProjetoEntity.class));
    }

}
