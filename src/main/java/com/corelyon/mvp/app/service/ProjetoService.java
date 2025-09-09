package com.corelyon.mvp.app.service;

import com.corelyon.mvp.app.dto.ProjetoRequest;
import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.app.usecase.projeto.BuscarProjetoUseCase;
import com.corelyon.mvp.app.usecase.projeto.CriarProjetoUseCase;
import com.corelyon.mvp.app.usecase.projeto.ListarProjetosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjetoService {
    
    @Autowired
    private CriarProjetoUseCase criarProjetoUseCase;
    
    @Autowired
    private ListarProjetosUseCase listarProjetosUseCase;
    
    @Autowired
    private BuscarProjetoUseCase buscarProjetoUseCase;
    
    public ProjetoResponse criarProjeto(ProjetoRequest request) {
        return criarProjetoUseCase.executar(request);
    }
    
    @Transactional(readOnly = true)
    public List<ProjetoResponse> listarProjetos() {
        return listarProjetosUseCase.executar();
    }
    
    @Transactional(readOnly = true)
    public ProjetoResponse buscarPorId(Long id) {
        return buscarProjetoUseCase.executar(id);
    }
}
