package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.domain.Funcionario;
import com.corelyon.mvp.domain.repository.FuncionarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListarFuncionariosUseCase {
    
    private final FuncionarioRepository funcionarioRepository;
    
    public ListarFuncionariosUseCase(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }
    
    public List<FuncionarioResponse> executar() {
        List<Funcionario> funcionarios = funcionarioRepository.listarTodos();
        
        return funcionarios.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
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
