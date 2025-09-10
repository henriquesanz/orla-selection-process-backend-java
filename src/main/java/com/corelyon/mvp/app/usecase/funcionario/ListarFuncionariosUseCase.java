package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.infra.entity.FuncionarioEntity;
import com.corelyon.mvp.infra.repository.FuncionarioRepositoryJpa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListarFuncionariosUseCase {
    
    private final FuncionarioRepositoryJpa funcionarioRepositoryJpa;
    
    public ListarFuncionariosUseCase(FuncionarioRepositoryJpa funcionarioRepositoryJpa) {
        this.funcionarioRepositoryJpa = funcionarioRepositoryJpa;
    }
    
    public List<FuncionarioResponse> executar() {
        List<FuncionarioEntity> funcionarios = funcionarioRepositoryJpa.findAll();
        
        return funcionarios.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    private FuncionarioResponse toResponse(FuncionarioEntity funcionarioEntity) {
        return new FuncionarioResponse(
            funcionarioEntity.getId(),
            funcionarioEntity.getNome(),
            funcionarioEntity.getCpf(),
            funcionarioEntity.getEmail(),
            funcionarioEntity.getSalario(),
            funcionarioEntity.getProjetos() != null ? 
                funcionarioEntity.getProjetos().stream()
                    .map(p -> new com.corelyon.mvp.app.dto.ProjetoResponse(
                        p.getId(),
                        p.getNome(),
                        p.getDescricao(),
                        p.getDataCriacao(),
                        null // funcionários não são carregados aqui para evitar referência circular
                    ))
                    .collect(java.util.stream.Collectors.toSet()) : 
                null
        );
    }
}
