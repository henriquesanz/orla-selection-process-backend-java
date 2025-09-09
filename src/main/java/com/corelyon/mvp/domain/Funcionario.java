package com.corelyon.mvp.domain;

import java.util.HashSet;
import java.util.Set;

public record Funcionario(Long id, String nome, String cpf, String email, Double salario, Set<Projeto> projetos) {
    public Funcionario(Long id, String nome, String cpf, String email, Double salario) {
        this(id, nome, cpf, email, salario, new HashSet<>());
    }
}