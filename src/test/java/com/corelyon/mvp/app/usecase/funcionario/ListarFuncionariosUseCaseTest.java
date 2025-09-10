package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.infra.entity.FuncionarioEntity;
import com.corelyon.mvp.infra.repository.FuncionarioRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarFuncionariosUseCaseTest {

    @Mock
    private FuncionarioRepositoryJpa funcionarioRepositoryJpa;

    @InjectMocks
    private ListarFuncionariosUseCase listarFuncionariosUseCase;

    @Test
    void deveListarFuncionariosComSucesso() {
        FuncionarioEntity funcionario1 = new FuncionarioEntity("João Silva", "12345678901", "joao@email.com", 5000.0);
        funcionario1.setId(1L);
        FuncionarioEntity funcionario2 = new FuncionarioEntity("Maria Santos", "98765432100", "maria@email.com", 6000.0);
        funcionario2.setId(2L);
        List<FuncionarioEntity> funcionarios = Arrays.asList(funcionario1, funcionario2);

        when(funcionarioRepositoryJpa.findAll()).thenReturn(funcionarios);

        List<FuncionarioResponse> response = listarFuncionariosUseCase.executar();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("João Silva", response.get(0).nome());
        assertEquals("12345678901", response.get(0).cpf());
        assertEquals("joao@email.com", response.get(0).email());
        assertEquals(5000.0, response.get(0).salario());
        assertEquals("Maria Santos", response.get(1).nome());
        assertEquals("98765432100", response.get(1).cpf());
        assertEquals("maria@email.com", response.get(1).email());
        assertEquals(6000.0, response.get(1).salario());

        verify(funcionarioRepositoryJpa).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverFuncionarios() {
        when(funcionarioRepositoryJpa.findAll()).thenReturn(Arrays.asList());

        List<FuncionarioResponse> response = listarFuncionariosUseCase.executar();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(funcionarioRepositoryJpa).findAll();
    }
}
