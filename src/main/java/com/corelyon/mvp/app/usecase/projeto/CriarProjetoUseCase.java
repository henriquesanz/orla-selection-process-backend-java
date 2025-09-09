package com.corelyon.mvp.app.usecase.projeto;

import com.corelyon.mvp.app.dto.ProjetoRequest;
import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.domain.Funcionario;
import com.corelyon.mvp.domain.Projeto;
import com.corelyon.mvp.domain.repository.FuncionarioRepository;
import com.corelyon.mvp.domain.repository.ProjetoRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CriarProjetoUseCase {
    
    private final ProjetoRepository projetoRepository;
    private final FuncionarioRepository funcionarioRepository;
    
    public CriarProjetoUseCase(ProjetoRepository projetoRepository, 
                               FuncionarioRepository funcionarioRepository) {
        this.projetoRepository = projetoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }
    
    public ProjetoResponse executar(ProjetoRequest request) {
        // Validações de negócio
        if (projetoRepository.existePorNome(request.nome())) {
            throw new RuntimeException("Projeto com nome " + request.nome() + " já existe");
        }
        
        // Criar projeto
        Projeto projeto = new Projeto(
            null, // ID será gerado pelo banco
            request.nome(),
            request.descricao(),
            java.time.LocalDateTime.now()
        );
        
        // Associar funcionários se fornecidos
        if (request.funcionarios() != null && !request.funcionarios().isEmpty()) {
            Set<Funcionario> funcionarios = new HashSet<>();
            for (var funcionarioRequest : request.funcionarios()) {
                Funcionario funcionario = buscarOuCriarFuncionario(funcionarioRequest);
                funcionarios.add(funcionario);
            }
            projeto = new Projeto(
                projeto.id(),
                projeto.nome(),
                projeto.descricao(),
                projeto.dataCriacao()
            );
        }
        
        Projeto projetoSalvo = projetoRepository.salvar(projeto);
        
        return toResponse(projetoSalvo);
    }
    
    private Funcionario buscarOuCriarFuncionario(com.corelyon.mvp.app.dto.FuncionarioRequest funcionarioRequest) {
        return funcionarioRepository.buscarPorCpf(funcionarioRequest.cpf())
            .orElseGet(() -> {
                if (funcionarioRepository.existePorEmail(funcionarioRequest.email())) {
                    throw new RuntimeException("Funcionário com email " + funcionarioRequest.email() + " já existe");
                }
                
                Funcionario novoFuncionario = new Funcionario(
                    null,
                    funcionarioRequest.nome(),
                    funcionarioRequest.cpf(),
                    funcionarioRequest.email(),
                    funcionarioRequest.salario()
                );
                return funcionarioRepository.salvar(novoFuncionario);
            });
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
