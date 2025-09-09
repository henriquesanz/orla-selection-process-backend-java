package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.domain.Funcionario;
import com.corelyon.mvp.domain.repository.FuncionarioRepository;
import org.springframework.stereotype.Component;

@Component
public class BuscarFuncionarioUseCase {
    
    private final FuncionarioRepository funcionarioRepository;
    
    public BuscarFuncionarioUseCase(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }
    
    public FuncionarioResponse executar(Long id) {
        Funcionario funcionario = funcionarioRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com ID: " + id));
        
        return toResponse(funcionario);
    }
    
    private FuncionarioResponse toResponse(Funcionario funcionario) {
        return new FuncionarioResponse(
            funcionario.id(),
            funcionario.nome(),
            funcionario.cpf(),
            funcionario.email(),
            funcionario.salario(),
            null // Projetos serão carregados separadamente se necessário
        );
    }
}
