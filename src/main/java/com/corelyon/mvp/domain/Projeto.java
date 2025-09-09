package com.corelyon.mvp.domain;

import java.time.LocalDateTime;

public record Projeto(Long id, String nome, String descricao, LocalDateTime dataCriacao) {}
