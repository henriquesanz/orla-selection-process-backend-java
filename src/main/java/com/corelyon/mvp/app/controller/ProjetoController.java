package com.corelyon.mvp.app.controller;

import com.corelyon.mvp.app.dto.ProjetoRequest;
import com.corelyon.mvp.app.dto.ProjetoResponse;
import com.corelyon.mvp.app.service.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
@CrossOrigin(origins = "*")
public class ProjetoController {
    
    @Autowired
    private ProjetoService projetoService;
    
    @PostMapping
    public ResponseEntity<ProjetoResponse> criarProjeto(@Valid @RequestBody ProjetoRequest request) {
        try {
            ProjetoResponse response = projetoService.criarProjeto(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ProjetoResponse>> listarProjetos() {
        List<ProjetoResponse> projetos = projetoService.listarProjetos();
        return ResponseEntity.ok(projetos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponse> buscarProjeto(@PathVariable Long id) {
        try {
            ProjetoResponse response = projetoService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
