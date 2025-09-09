package com.corelyon.mvp.app.controller;

import com.corelyon.mvp.app.dto.FuncionarioRequest;
import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.corelyon.mvp.app.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {
    
    @Autowired
    private FuncionarioService funcionarioService;
    
    @PostMapping
    public ResponseEntity<FuncionarioResponse> criarFuncionario(@Valid @RequestBody FuncionarioRequest request) {
        try {
            FuncionarioResponse response = funcionarioService.criarFuncionario(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<FuncionarioResponse>> listarFuncionarios() {
        List<FuncionarioResponse> funcionarios = funcionarioService.listarFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarFuncionario(@PathVariable Long id) {
        try {
            FuncionarioResponse response = funcionarioService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
