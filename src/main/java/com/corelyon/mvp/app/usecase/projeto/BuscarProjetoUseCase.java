package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.app.exception.ResourceNotFoundException;
import com.corelyon.mvp.infra.entity.ProjetoEntity;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
import org.springframework.stereotype.Component;

@Component
public class BuscarProjetoUseCase {
    
    private final ProjetoRepositoryJpa projetoRepositoryJpa;
    
    public BuscarProjetoUseCase(ProjetoRepositoryJpa projetoRepositoryJpa) {
        this.projetoRepositoryJpa = projetoRepositoryJpa;
    }
    
    public ProjetoResponse executar(Long id) {
        ProjetoEntity projetoEntity = projetoRepositoryJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com ID: " + id));
        
        return toResponse(projetoEntity);
    }
    
    private ProjetoResponse toResponse(ProjetoEntity projetoEntity) {
        return new ProjetoResponse(
            projetoEntity.getId(),
            projetoEntity.getNome(),
            projetoEntity.getDescricao(),
            projetoEntity.getDataCriacao(),
            projetoEntity.getFuncionarios() != null ? 
                projetoEntity.getFuncionarios().stream()
                    .map(f -> new FuncionarioResponse(
                        f.getId(),
                        f.getNome(),
                        f.getCpf(),
                        f.getEmail(),
                        f.getSalario(),
                        null // projetos não são carregados aqui para evitar referência circular
                    ))
                    .collect(java.util.stream.Collectors.toSet()) : 
                null
        );
    }
}
