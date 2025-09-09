package com.corelyon.mvp.domain.repository;

import com.corelyon.mvp.domain.Funcionario;
import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository {
    Funcionario salvar(Funcionario funcionario);
    Optional<Funcionario> buscarPorId(Long id);
    List<Funcionario> listarTodos();
    boolean existePorCpf(String cpf);
    boolean existePorEmail(String email);
    Optional<Funcionario> buscarPorCpf(String cpf);
    void deletar(Long id);
}
