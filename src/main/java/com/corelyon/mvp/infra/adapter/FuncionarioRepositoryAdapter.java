package com.corelyon.mvp.infra.adapter;

import com.corelyon.mvp.domain.Funcionario;
import com.corelyon.mvp.domain.repository.FuncionarioRepository;
import com.corelyon.mvp.infra.entity.FuncionarioEntity;
import com.corelyon.mvp.infra.repository.FuncionarioRepositoryJpa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FuncionarioRepositoryAdapter implements FuncionarioRepository {
    
    private final FuncionarioRepositoryJpa funcionarioRepositoryJpa;
    
    public FuncionarioRepositoryAdapter(FuncionarioRepositoryJpa funcionarioRepositoryJpa) {
        this.funcionarioRepositoryJpa = funcionarioRepositoryJpa;
    }
    
    @Override
    public Funcionario salvar(Funcionario funcionario) {
        FuncionarioEntity entity = toEntity(funcionario);
        FuncionarioEntity savedEntity = funcionarioRepositoryJpa.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepositoryJpa.findById(id)
            .map(this::toDomain);
    }
    
    @Override
    public List<Funcionario> listarTodos() {
        return funcionarioRepositoryJpa.findAll()
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existePorCpf(String cpf) {
        return funcionarioRepositoryJpa.existsByCpf(cpf);
    }
    
    @Override
    public boolean existePorEmail(String email) {
        return funcionarioRepositoryJpa.existsByEmail(email);
    }
    
    @Override
    public Optional<Funcionario> buscarPorCpf(String cpf) {
        return funcionarioRepositoryJpa.findByCpf(cpf)
            .map(this::toDomain);
    }
    
    @Override
    public void deletar(Long id) {
        funcionarioRepositoryJpa.deleteById(id);
    }
    
    private FuncionarioEntity toEntity(Funcionario funcionario) {
        FuncionarioEntity entity = new FuncionarioEntity(
            funcionario.nome(),
            funcionario.cpf(),
            funcionario.email(),
            funcionario.salario()
        );
        
        if (funcionario.id() != null) {
            entity.setId(funcionario.id());
        }
        
        return entity;
    }
    
    private Funcionario toDomain(FuncionarioEntity entity) {
        return new Funcionario(
            entity.getId(),
            entity.getNome(),
            entity.getCpf(),
            entity.getEmail(),
            entity.getSalario(),
            null
        );
    }
}
