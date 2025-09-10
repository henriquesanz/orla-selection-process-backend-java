package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.infra.entity.ProjetoEntity;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListarProjetosUseCase {
    
    private final ProjetoRepositoryJpa projetoRepositoryJpa;
    
    public ListarProjetosUseCase(ProjetoRepositoryJpa projetoRepositoryJpa) {
        this.projetoRepositoryJpa = projetoRepositoryJpa;
    }
    
    public List<ProjetoResponse> executar() {
        List<ProjetoEntity> projetos = projetoRepositoryJpa.findAll();
        
        return projetos.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    private ProjetoResponse toResponse(ProjetoEntity projetoEntity) {
        return new ProjetoResponse(
            projetoEntity.getId(),
            projetoEntity.getNome(),
            projetoEntity.getDescricao(),
            projetoEntity.getDataCriacao(),
            projetoEntity.getFuncionarios() != null ? 
                projetoEntity.getFuncionarios().stream()
                    .map(f -> new com.corelyon.mvp.app.dto.FuncionarioResponse(
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
