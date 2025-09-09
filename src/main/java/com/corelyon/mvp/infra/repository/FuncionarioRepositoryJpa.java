package com.corelyon.mvp.infra.repository;

import com.corelyon.mvp.infra.entity.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepositoryJpa extends JpaRepository<FuncionarioEntity, Long> {
    
    Optional<FuncionarioEntity> findByCpf(String cpf);
    
    Optional<FuncionarioEntity> findByEmail(String email);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByEmail(String email);
    
    List<FuncionarioEntity> findByNomeContainingIgnoreCase(String nome);
    
    List<FuncionarioEntity> findBySalarioGreaterThan(Double salario);
    
    List<FuncionarioEntity> findBySalarioBetween(Double salarioMin, Double salarioMax);
    
    @Query("SELECT f FROM FuncionarioEntity f JOIN f.projetos p WHERE p.id = :projetoId")
    List<FuncionarioEntity> findByProjetoId(@Param("projetoId") Long projetoId);
    
    @Query("SELECT COUNT(f) FROM FuncionarioEntity f WHERE f.salario BETWEEN :salarioMin AND :salarioMax")
    Long countByFaixaSalarial(@Param("salarioMin") Double salarioMin, @Param("salarioMax") Double salarioMax);
    
    List<FuncionarioEntity> findAllByOrderBySalarioDesc();
    
    List<FuncionarioEntity> findAllByOrderByNomeAsc();
}
