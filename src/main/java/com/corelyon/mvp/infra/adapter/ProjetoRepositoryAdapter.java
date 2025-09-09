package com.corelyon.mvp.infra.adapter;

import com.corelyon.mvp.domain.Projeto;
import com.corelyon.mvp.domain.repository.ProjetoRepository;
import com.corelyon.mvp.infra.entity.ProjetoEntity;
import com.corelyon.mvp.infra.repository.ProjetoRepositoryJpa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProjetoRepositoryAdapter implements ProjetoRepository {
    
    private final ProjetoRepositoryJpa projetoRepositoryJpa;
    
    public ProjetoRepositoryAdapter(ProjetoRepositoryJpa projetoRepositoryJpa) {
        this.projetoRepositoryJpa = projetoRepositoryJpa;
    }
    
    @Override
    public Projeto salvar(Projeto projeto) {
        ProjetoEntity entity = toEntity(projeto);
        ProjetoEntity savedEntity = projetoRepositoryJpa.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<Projeto> buscarPorId(Long id) {
        return projetoRepositoryJpa.findById(id)
            .map(this::toDomain);
    }
    
    @Override
    public List<Projeto> listarTodos() {
        return projetoRepositoryJpa.findAll()
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existePorNome(String nome) {
        return projetoRepositoryJpa.existsByNome(nome);
    }
    
    @Override
    public Optional<Projeto> buscarPorNome(String nome) {
        return projetoRepositoryJpa.findByNome(nome)
            .map(this::toDomain);
    }
    
    @Override
    public void deletar(Long id) {
        projetoRepositoryJpa.deleteById(id);
    }
    
    private ProjetoEntity toEntity(Projeto projeto) {
        ProjetoEntity entity = new ProjetoEntity(
            projeto.nome(),
            projeto.descricao()
        );
        
        if (projeto.id() != null) {
            entity.setId(projeto.id());
        }
        
        if (projeto.dataCriacao() != null) {
            entity.setDataCriacao(projeto.dataCriacao());
        }
        
        return entity;
    }
    
    private Projeto toDomain(ProjetoEntity entity) {
        return new Projeto(
            entity.getId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getDataCriacao()
        );
    }
}
