package com.corelyon.mvp.infra.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projetos")
public class ProjetoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @ManyToMany(mappedBy = "projetos", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<FuncionarioEntity> funcionarios = new HashSet<>();
    
    public ProjetoEntity() {}
    
    public ProjetoEntity(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
    }
    
    public ProjetoEntity(String nome, String descricao, LocalDateTime dataCriacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public Set<FuncionarioEntity> getFuncionarios() {
        return funcionarios;
    }
    
    public void setFuncionarios(Set<FuncionarioEntity> funcionarios) {
        this.funcionarios = funcionarios;
    }
    
    public void adicionarFuncionario(FuncionarioEntity funcionario) {
        this.funcionarios.add(funcionario);
        funcionario.getProjetos().add(this);
    }
    
    public void removerFuncionario(FuncionarioEntity funcionario) {
        this.funcionarios.remove(funcionario);
        funcionario.getProjetos().remove(this);
    }
}
