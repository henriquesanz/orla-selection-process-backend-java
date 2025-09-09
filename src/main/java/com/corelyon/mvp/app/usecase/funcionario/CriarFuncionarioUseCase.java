package com.corelyon.mvp.app.usecase.funcionario;

import com.corelyon.mvp.app.dto.FuncionarioRequest;
import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.domain.Funcionario;
import com.corelyon.mvp.domain.Projeto;
import com.corelyon.mvp.domain.repository.FuncionarioRepository;
import com.corelyon.mvp.domain.repository.ProjetoRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CriarFuncionarioUseCase {
    
    private final FuncionarioRepository funcionarioRepository;
    private final ProjetoRepository projetoRepository;
    
    public CriarFuncionarioUseCase(FuncionarioRepository funcionarioRepository, 
                                   ProjetoRepository projetoRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.projetoRepository = projetoRepository;
    }
    
    public FuncionarioResponse executar(FuncionarioRequest request) {
        // Validações de negócio
        if (funcionarioRepository.existePorCpf(request.cpf())) {
            throw new RuntimeException("Funcionário com CPF " + request.cpf() + " já existe");
        }
        
        if (funcionarioRepository.existePorEmail(request.email())) {
            throw new RuntimeException("Funcionário com email " + request.email() + " já existe");
        }
        
        // Criar funcionário
        Funcionario funcionario = new Funcionario(
            null, // ID será gerado pelo banco
            request.nome(),
            request.cpf(),
            request.email(),
            request.salario()
        );
        
        // Associar projetos se fornecidos
        if (request.projetos() != null && !request.projetos().isEmpty()) {
            Set<Projeto> projetos = new HashSet<>();
            for (var projetoRequest : request.projetos()) {
                Projeto projeto = buscarOuCriarProjeto(projetoRequest);
                projetos.add(projeto);
            }
            funcionario = new Funcionario(
                funcionario.id(),
                funcionario.nome(),
                funcionario.cpf(),
                funcionario.email(),
                funcionario.salario(),
                projetos
            );
        }
        
        Funcionario funcionarioSalvo = funcionarioRepository.salvar(funcionario);
        
        return toResponse(funcionarioSalvo);
    }
    
    private Projeto buscarOuCriarProjeto(com.corelyon.mvp.app.dto.ProjetoRequest projetoRequest) {
        return projetoRepository.buscarPorNome(projetoRequest.nome())
            .orElseGet(() -> {
                Projeto novoProjeto = new Projeto(
                    null,
                    projetoRequest.nome(),
                    projetoRequest.descricao(),
                    java.time.LocalDateTime.now()
                );
                return projetoRepository.salvar(novoProjeto);
            });
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
