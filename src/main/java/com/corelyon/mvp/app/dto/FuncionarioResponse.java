package com.corelyon.mvp.app.dto;

import java.util.Set;

public record FuncionarioResponse(
    Long id,
    String nome,
    String cpf,
    String email,
    Double salario,
    Set<ProjetoResponse> projetos
) {}
