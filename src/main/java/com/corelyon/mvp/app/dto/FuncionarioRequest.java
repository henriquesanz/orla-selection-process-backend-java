package com.corelyon.mvp.app.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FuncionarioRequest(
    @NotBlank(message = "Nome é obrigatório")
    String nome,
    
    @NotBlank(message = "CPF é obrigatório")
    String cpf,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    String email,
    
    @NotNull(message = "Salário é obrigatório")
    @Positive(message = "Salário deve ser positivo")
    Double salario,

    List<ProjetoRequest> projetos
) {}
