package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.domain.Projeto;
import com.corelyon.mvp.domain.repository.ProjetoRepository;
import org.springframework.stereotype.Component;

@Component
public class BuscarProjetoUseCase {
    
    private final ProjetoRepository projetoRepository;
    
    public BuscarProjetoUseCase(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }
    
    public ProjetoResponse executar(Long id) {
        Projeto projeto = projetoRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado com ID: " + id));
        
        return toResponse(projeto);
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
