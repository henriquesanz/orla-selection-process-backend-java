package com.corelyon.mvp.infra.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "funcionarios")
public class FuncionarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private Double salario;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "funcionario_projeto",
        joinColumns = @JoinColumn(name = "funcionario_id"),
        inverseJoinColumns = @JoinColumn(name = "projeto_id")
    )
    private Set<ProjetoEntity> projetos = new HashSet<>();
    
    public FuncionarioEntity() {}
    
    public FuncionarioEntity(String nome, String cpf, String email, Double salario) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.salario = salario;
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
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Double getSalario() {
        return salario;
    }
    
    public void setSalario(Double salario) {
        this.salario = salario;
    }
    
    public Set<ProjetoEntity> getProjetos() {
        return projetos;
    }
    
    public void setProjetos(Set<ProjetoEntity> projetos) {
        this.projetos = projetos;
    }
    
    public void adicionarProjeto(ProjetoEntity projeto) {
        this.projetos.add(projeto);
        projeto.getFuncionarios().add(this);
    }
    
    public void removerProjeto(ProjetoEntity projeto) {
        this.projetos.remove(projeto);
        projeto.getFuncionarios().remove(this);
    }
}
