package com.corelyon.mvp.infra.repository;

import com.corelyon.mvp.infra.entity.ProjetoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetoRepositoryJpa extends JpaRepository<ProjetoEntity, Long> {
    
    Optional<ProjetoEntity> findByNome(String nome);
    
    boolean existsByNome(String nome);
    
    List<ProjetoEntity> findByNomeContainingIgnoreCase(String nome);
    
    List<ProjetoEntity> findByDescricaoContainingIgnoreCase(String descricao);
    
    List<ProjetoEntity> findByDataCriacaoAfter(LocalDateTime data);
    
    List<ProjetoEntity> findByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    
    List<ProjetoEntity> findAllByOrderByDataCriacaoDesc();
    
    List<ProjetoEntity> findAllByOrderByNomeAsc();
    
    @Query("SELECT p FROM ProjetoEntity p JOIN p.funcionarios f WHERE f.id = :funcionarioId")
    List<ProjetoEntity> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);
    
    @Query("SELECT COUNT(p) FROM ProjetoEntity p WHERE p.dataCriacao BETWEEN :dataInicio AND :dataFim")
    Long countByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT p FROM ProjetoEntity p WHERE p.funcionarios IS EMPTY")
    List<ProjetoEntity> findProjetosSemFuncionarios();
    
    @Query("SELECT p FROM ProjetoEntity p WHERE SIZE(p.funcionarios) > :quantidade")
    List<ProjetoEntity> findProjetosComMaisFuncionariosQue(@Param("quantidade") int quantidade);
    
    @Query("SELECT p FROM ProjetoEntity p WHERE YEAR(p.dataCriacao) = :ano AND MONTH(p.dataCriacao) = :mes")
    List<ProjetoEntity> findProjetosDoMes(@Param("ano") int ano, @Param("mes") int mes);
}
