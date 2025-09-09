package com.corelyon.mvp.app.service;

import com.corelyon.mvp.app.dto.FuncionarioRequest;
import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.app.usecase.funcionario.BuscarFuncionarioUseCase;
import com.corelyon.mvp.app.usecase.funcionario.CriarFuncionarioUseCase;
import com.corelyon.mvp.app.usecase.funcionario.ListarFuncionariosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FuncionarioService {
    
    @Autowired
    private CriarFuncionarioUseCase criarFuncionarioUseCase;
    
    @Autowired
    private ListarFuncionariosUseCase listarFuncionariosUseCase;
    
    @Autowired
    private BuscarFuncionarioUseCase buscarFuncionarioUseCase;
    
    public FuncionarioResponse criarFuncionario(FuncionarioRequest request) {
        return criarFuncionarioUseCase.executar(request);
    }
    
    @Transactional(readOnly = true)
    public List<FuncionarioResponse> listarFuncionarios() {
        return listarFuncionariosUseCase.executar();
    }
    
    @Transactional(readOnly = true)
    public FuncionarioResponse buscarPorId(Long id) {
        return buscarFuncionarioUseCase.executar(id);
    }
}
