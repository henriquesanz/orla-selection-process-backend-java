package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.infra.entity.ProjetoEntity;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarProjetosUseCaseTest {

    @Mock
    private ProjetoRepositoryJpa projetoRepositoryJpa;

    @InjectMocks
    private ListarProjetosUseCase listarProjetosUseCase;

    @Test
    void deveListarProjetosComSucesso() {
        LocalDateTime dataCriacao = LocalDateTime.now();
        ProjetoEntity projeto1 = new ProjetoEntity("Projeto 1", "Descrição do projeto 1", dataCriacao);
        projeto1.setId(1L);
        ProjetoEntity projeto2 = new ProjetoEntity("Projeto 2", "Descrição do projeto 2", dataCriacao);
        projeto2.setId(2L);
        List<ProjetoEntity> projetos = Arrays.asList(projeto1, projeto2);

        when(projetoRepositoryJpa.findAll()).thenReturn(projetos);

        List<ProjetoResponse> response = listarProjetosUseCase.executar();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Projeto 1", response.get(0).nome());
        assertEquals("Descrição do projeto 1", response.get(0).descricao());
        assertEquals(dataCriacao, response.get(0).dataCriacao());
        assertEquals("Projeto 2", response.get(1).nome());
        assertEquals("Descrição do projeto 2", response.get(1).descricao());
        assertEquals(dataCriacao, response.get(1).dataCriacao());

        verify(projetoRepositoryJpa).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverProjetos() {
        when(projetoRepositoryJpa.findAll()).thenReturn(Arrays.asList());

        List<ProjetoResponse> response = listarProjetosUseCase.executar();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(projetoRepositoryJpa).findAll();
    }
}
