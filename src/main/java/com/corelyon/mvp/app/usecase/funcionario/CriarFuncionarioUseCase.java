package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioRequest;
import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.infra.entity.FuncionarioEntity;
import com.corelyon.mvp.infra.entity.ProjetoEntity;
import com.corelyon.mvp.infra.repository.FuncionarioRepositoryJpa;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
import org.springframework.stereotype.Component;

@Component
public class CriarFuncionarioUseCase {
    
    private final FuncionarioRepositoryJpa funcionarioRepositoryJpa;
    private final ProjetoRepositoryJpa projetoRepositoryJpa;
    
    public CriarFuncionarioUseCase(FuncionarioRepositoryJpa funcionarioRepositoryJpa, 
                                   ProjetoRepositoryJpa projetoRepositoryJpa) {
        this.funcionarioRepositoryJpa = funcionarioRepositoryJpa;
        this.projetoRepositoryJpa = projetoRepositoryJpa;
    }
    
    public FuncionarioResponse executar(FuncionarioRequest request) {
        if (funcionarioRepositoryJpa.existsByCpf(request.cpf())) {
            throw new RuntimeException("Funcionário com CPF " + request.cpf() + " já existe");
        }
        
        if (funcionarioRepositoryJpa.existsByEmail(request.email())) {
            throw new RuntimeException("Funcionário com email " + request.email() + " já existe");
        }
        
        FuncionarioEntity funcionarioEntity = new FuncionarioEntity(
            request.nome(),
            request.cpf(),
            request.email(),
            request.salario()
        );
        
        if (request.projetos() != null && !request.projetos().isEmpty()) {
            for (var projetoRequest : request.projetos()) {
                ProjetoEntity projeto = buscarOuCriarProjeto(projetoRequest);
                funcionarioEntity.adicionarProjeto(projeto);
            }
        }
        
        FuncionarioEntity funcionarioSalvo = funcionarioRepositoryJpa.save(funcionarioEntity);
        
        return toResponse(funcionarioSalvo);
    }
    
    private ProjetoEntity buscarOuCriarProjeto(com.corelyon.mvp.app.dto.ProjetoRequest projetoRequest) {
        return projetoRepositoryJpa.findByNome(projetoRequest.nome())
            .orElseGet(() -> {
                ProjetoEntity novoProjeto = new ProjetoEntity(
                    projetoRequest.nome(),
                    projetoRequest.descricao(),
                    java.time.LocalDateTime.now()
                );
                return projetoRepositoryJpa.save(novoProjeto);
            });
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
                        null
                    ))
                    .collect(java.util.stream.Collectors.toSet()) : 
                null
        );
    }
}
