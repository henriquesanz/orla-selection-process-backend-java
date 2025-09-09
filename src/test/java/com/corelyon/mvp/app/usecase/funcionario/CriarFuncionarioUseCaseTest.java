package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioRequest;
import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.domain.Funcionario;
import com.corelyon.mvp.domain.repository.FuncionarioRepository;
import com.corelyon.mvp.domain.repository.ProjetoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarFuncionarioUseCaseTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private CriarFuncionarioUseCase criarFuncionarioUseCase;

    private FuncionarioRequest funcionarioRequest;

    @BeforeEach
    void setUp() {
        funcionarioRequest = new FuncionarioRequest(
            "João Silva",
            "12345678901",
            "joao@email.com",
            5000.0,
            null
        );
    }

    @Test
    void deveCriarFuncionarioComSucesso() {
        // Arrange
        when(funcionarioRepository.existePorCpf(anyString())).thenReturn(false);
        when(funcionarioRepository.existePorEmail(anyString())).thenReturn(false);
        when(funcionarioRepository.salvar(any(Funcionario.class))).thenReturn(
            new Funcionario(1L, "João Silva", "12345678901", "joao@email.com", 5000.0)
        );

        // Act
        FuncionarioResponse response = criarFuncionarioUseCase.executar(funcionarioRequest);

        // Assert
        assertNotNull(response);
        assertEquals("João Silva", response.nome());
        assertEquals("12345678901", response.cpf());
        assertEquals("joao@email.com", response.email());
        assertEquals(5000.0, response.salario());
        
        verify(funcionarioRepository).existePorCpf("12345678901");
        verify(funcionarioRepository).existePorEmail("joao@email.com");
        verify(funcionarioRepository).salvar(any(Funcionario.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        // Arrange
        when(funcionarioRepository.existePorCpf(anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> criarFuncionarioUseCase.executar(funcionarioRequest));
        
        assertEquals("Funcionário com CPF 12345678901 já existe", exception.getMessage());
        verify(funcionarioRepository).existePorCpf("12345678901");
        verify(funcionarioRepository, never()).salvar(any(Funcionario.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        when(funcionarioRepository.existePorCpf(anyString())).thenReturn(false);
        when(funcionarioRepository.existePorEmail(anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> criarFuncionarioUseCase.executar(funcionarioRequest));
        
        assertEquals("Funcionário com email joao@email.com já existe", exception.getMessage());
        verify(funcionarioRepository).existePorCpf("12345678901");
        verify(funcionarioRepository).existePorEmail("joao@email.com");
        verify(funcionarioRepository, never()).salvar(any(Funcionario.class));
    }
}
