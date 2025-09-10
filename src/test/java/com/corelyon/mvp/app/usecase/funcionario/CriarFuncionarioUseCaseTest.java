package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioRequest;
import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.infra.entity.FuncionarioEntity;
import com.corelyon.mvp.infra.repository.FuncionarioRepositoryJpa;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
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
    private FuncionarioRepositoryJpa funcionarioRepositoryJpa;

    @Mock
    private ProjetoRepositoryJpa projetoRepositoryJpa;

    @InjectMocks
    private CriarFuncionarioUseCase criarFuncionarioUseCase;

    private FuncionarioRequest funcionarioRequest;

    @BeforeEach
    void setUp() {
        funcionarioRequest = new FuncionarioRequest(
            "João Silva",
            "11144477735",
            "joao@email.com",
            5000.0,
            null
        );
    }

    @Test
    void deveCriarFuncionarioComSucesso() {
        when(funcionarioRepositoryJpa.existsByCpf(anyString())).thenReturn(false);
        when(funcionarioRepositoryJpa.existsByEmail(anyString())).thenReturn(false);
        FuncionarioEntity funcionarioSalvo = new FuncionarioEntity("João Silva", "11144477735", "joao@email.com", 5000.0);
        funcionarioSalvo.setId(1L);
        when(funcionarioRepositoryJpa.save(any(FuncionarioEntity.class))).thenReturn(funcionarioSalvo);

        FuncionarioResponse response = criarFuncionarioUseCase.executar(funcionarioRequest);

        assertNotNull(response);
        assertEquals("João Silva", response.nome());
        assertEquals("11144477735", response.cpf());
        assertEquals("joao@email.com", response.email());
        assertEquals(5000.0, response.salario());
        
        verify(funcionarioRepositoryJpa).existsByCpf("11144477735");
        verify(funcionarioRepositoryJpa).existsByEmail("joao@email.com");
        verify(funcionarioRepositoryJpa).save(any(FuncionarioEntity.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        when(funcionarioRepositoryJpa.existsByCpf(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> criarFuncionarioUseCase.executar(funcionarioRequest));
        
        assertEquals("Funcionário com CPF 11144477735 já existe", exception.getMessage());
        verify(funcionarioRepositoryJpa).existsByCpf("11144477735");
        verify(funcionarioRepositoryJpa, never()).save(any(FuncionarioEntity.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        when(funcionarioRepositoryJpa.existsByCpf(anyString())).thenReturn(false);
        when(funcionarioRepositoryJpa.existsByEmail(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> criarFuncionarioUseCase.executar(funcionarioRequest));
        
        assertEquals("Funcionário com email joao@email.com já existe", exception.getMessage());
        verify(funcionarioRepositoryJpa).existsByCpf("11144477735");
        verify(funcionarioRepositoryJpa).existsByEmail("joao@email.com");
        verify(funcionarioRepositoryJpa, never()).save(any(FuncionarioEntity.class));
    }

}
