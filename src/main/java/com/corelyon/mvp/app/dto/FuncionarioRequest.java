package com.corelyon.mvp.app.dto;

import java.util.List;

import org.hibernate.validator.constraints.br.CPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FuncionarioRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String nome,
    
    @NotBlank(message = "CPF é obrigatório")
    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres")
    @CPF(message = "CPF inválido")
    String cpf,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    String email,
    
    @NotNull(message = "Salário é obrigatório")
    @Positive(message = "Salário deve ser positivo")
    Double salario,

    List<ProjetoRequest> projetos
) {}
