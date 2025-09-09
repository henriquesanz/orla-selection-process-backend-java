package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.domain.Projeto;
import com.corelyon.mvp.domain.repository.ProjetoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListarProjetosUseCase {
    
    private final ProjetoRepository projetoRepository;
    
    public ListarProjetosUseCase(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }
    
    public List<ProjetoResponse> executar() {
        List<Projeto> projetos = projetoRepository.listarTodos();
        
        return projetos.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    private ProjetoResponse toResponse(Projeto projeto) {
        return new ProjetoResponse(
            projeto.id(),
            projeto.nome(),
            projeto.descricao(),
            projeto.dataCriacao(),
            null // Funcionários serão carregados separadamente se necessário
        );
    }
}
