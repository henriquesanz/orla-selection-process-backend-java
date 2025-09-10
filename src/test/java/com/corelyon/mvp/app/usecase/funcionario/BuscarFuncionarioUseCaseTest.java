package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.domain.Funcionario;
import com.corelyon.mvp.domain.repository.FuncionarioRepository;
import com.corelyon.mvp.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarFuncionarioUseCaseTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private BuscarFuncionarioUseCase buscarFuncionarioUseCase;

    @Test
    void deveBuscarFuncionarioComSucesso() {
        Long funcionarioId = 1L;
        Funcionario funcionario = new Funcionario(1L, "João Silva", "12345678901", "joao@email.com", 5000.0);

        when(funcionarioRepository.buscarPorId(funcionarioId)).thenReturn(Optional.of(funcionario));

        FuncionarioResponse response = buscarFuncionarioUseCase.executar(funcionarioId);

        assertNotNull(response);
        assertEquals("João Silva", response.nome());
        assertEquals("12345678901", response.cpf());
        assertEquals("joao@email.com", response.email());
        assertEquals(5000.0, response.salario());

        verify(funcionarioRepository).buscarPorId(funcionarioId);
    }

    @Test
    void deveLancarExcecaoQuandoFuncionarioNaoExiste() {
        Long funcionarioId = 99999L;

        when(funcionarioRepository.buscarPorId(funcionarioId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> buscarFuncionarioUseCase.executar(funcionarioId));
        
        assertEquals("Funcionário não encontrado com ID: 99999", exception.getMessage());
        verify(funcionarioRepository).buscarPorId(funcionarioId);
    }
}
