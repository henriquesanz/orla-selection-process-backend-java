package com.corelyon.mvp.app.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record ProjetoRequest(
    @NotBlank(message = "Nome é obrigatório")
    String nome,
    
    String descricao,

    List<FuncionarioRequest> funcionarios
) {}
