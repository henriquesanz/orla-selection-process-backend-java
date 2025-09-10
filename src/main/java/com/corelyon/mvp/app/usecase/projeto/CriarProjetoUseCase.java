package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.FuncionarioRequest;
import com.corelyon.mvp.app.dto.ProjetoRequest;
import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.infra.entity.FuncionarioEntity;
import com.corelyon.mvp.infra.entity.ProjetoEntity;
import com.corelyon.mvp.infra.repository.FuncionarioRepositoryJpa;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
import org.springframework.stereotype.Component;

@Component
public class CriarProjetoUseCase {
    
    private final ProjetoRepositoryJpa projetoRepositoryJpa;
    private final FuncionarioRepositoryJpa funcionarioRepositoryJpa;
    
    public CriarProjetoUseCase(ProjetoRepositoryJpa projetoRepositoryJpa, 
                               FuncionarioRepositoryJpa funcionarioRepositoryJpa) {
        this.projetoRepositoryJpa = projetoRepositoryJpa;
        this.funcionarioRepositoryJpa = funcionarioRepositoryJpa;
    }
    
    public ProjetoResponse executar(ProjetoRequest request) {
        if (projetoRepositoryJpa.existsByNome(request.nome())) {
            throw new RuntimeException("Projeto com nome " + request.nome() + " já existe");
        }
        
        ProjetoEntity projetoEntity = new ProjetoEntity(
            request.nome(),
            request.descricao(),
            java.time.LocalDateTime.now()
        );
        
        if (request.funcionarios() != null && !request.funcionarios().isEmpty()) {
            for (var funcionarioRequest : request.funcionarios()) {
                FuncionarioEntity funcionario = buscarOuCriarFuncionario(funcionarioRequest);
                projetoEntity.adicionarFuncionario(funcionario);
            }
        }
        
        ProjetoEntity projetoSalvo = projetoRepositoryJpa.save(projetoEntity);
        
        return toResponse(projetoSalvo);
    }
    
    private FuncionarioEntity buscarOuCriarFuncionario(FuncionarioRequest funcionarioRequest) {
        return funcionarioRepositoryJpa.findByCpf(funcionarioRequest.cpf())
            .orElseGet(() -> {
                if (funcionarioRepositoryJpa.existsByEmail(funcionarioRequest.email())) {
                    throw new RuntimeException("Funcionário com email " + funcionarioRequest.email() + " já existe");
                }
                return new FuncionarioEntity(
                    funcionarioRequest.nome(),
                    funcionarioRequest.cpf(),
                    funcionarioRequest.email(),
                    funcionarioRequest.salario()
                );
            });
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
                        null
                    ))
                    .collect(java.util.stream.Collectors.toSet()) : 
                null
        );
    }
}
