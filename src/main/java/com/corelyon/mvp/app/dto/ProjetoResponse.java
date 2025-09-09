package com.corelyon.mvp.app.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record ProjetoResponse(
    Long id,
    String nome,
    String descricao,
    LocalDateTime dataCriacao,
    Set<FuncionarioResponse> funcionarios
) {}
