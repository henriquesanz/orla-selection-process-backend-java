package com.corelyon.mvp.domain.repository;

import com.corelyon.mvp.domain.Projeto;
import java.util.List;
import java.util.Optional;

public interface ProjetoRepository {
    Projeto salvar(Projeto projeto);
    Optional<Projeto> buscarPorId(Long id);
    List<Projeto> listarTodos();
    boolean existePorNome(String nome);
    Optional<Projeto> buscarPorNome(String nome);
    void deletar(Long id);
}
