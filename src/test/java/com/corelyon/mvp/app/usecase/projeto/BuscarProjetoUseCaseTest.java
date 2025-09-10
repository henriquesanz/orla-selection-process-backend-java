package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.infra.entity.ProjetoEntity;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
import com.corelyon.mvp.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarProjetoUseCaseTest {

    @Mock
    private ProjetoRepositoryJpa projetoRepositoryJpa;

    @InjectMocks
    private BuscarProjetoUseCase buscarProjetoUseCase;

    @Test
    void deveBuscarProjetoComSucesso() {
        Long projetoId = 1L;
        LocalDateTime dataCriacao = LocalDateTime.now();
        ProjetoEntity projeto = new ProjetoEntity("Projeto Teste", "Descrição do projeto", dataCriacao);
        projeto.setId(1L);

        when(projetoRepositoryJpa.findById(projetoId)).thenReturn(Optional.of(projeto));

        ProjetoResponse response = buscarProjetoUseCase.executar(projetoId);

        assertNotNull(response);
        assertEquals("Projeto Teste", response.nome());
        assertEquals("Descrição do projeto", response.descricao());
        assertEquals(dataCriacao, response.dataCriacao());

        verify(projetoRepositoryJpa).findById(projetoId);
    }

    @Test
    void deveLancarExcecaoQuandoProjetoNaoExiste() {
        Long projetoId = 99999L;

        when(projetoRepositoryJpa.findById(projetoId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> buscarProjetoUseCase.executar(projetoId));
        
        assertEquals("Projeto não encontrado com ID: 99999", exception.getMessage());
        verify(projetoRepositoryJpa).findById(projetoId);
    }
}
