package com.corelyon.mvp.integration;

import com.corelyon.mvp.app.dto.ProjetoRequest;
import com.corelyon.mvp.app.dto.ProjetoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class ProjetoControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @Test
    void deveCriarProjetoComSucesso() throws Exception {
        ProjetoRequest request = new ProjetoRequest(
            "Sistema de Gestão",
            "Sistema para gestão de funcionários e projetos",
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<ProjetoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ProjetoResponse> response = restTemplate.postForEntity(
            getBaseUrl() + "/projetos", entity, ProjetoResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sistema de Gestão", response.getBody().nome());
        assertEquals("Sistema para gestão de funcionários e projetos", response.getBody().descricao());
        assertNotNull(response.getBody().dataCriacao());
    }

    @Test
    void deveCriarProjetoSemDescricao() throws Exception {
        ProjetoRequest request = new ProjetoRequest(
            "Projeto Simples",
            null,
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<ProjetoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ProjetoResponse> response = restTemplate.postForEntity(
            getBaseUrl() + "/projetos", entity, ProjetoResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Projeto Simples", response.getBody().nome());
        assertNull(response.getBody().descricao());
        assertNotNull(response.getBody().dataCriacao());
    }

    @Test
    void deveListarProjetos() {
        ResponseEntity<ProjetoResponse[]> response = restTemplate.getForEntity(
            getBaseUrl() + "/projetos", ProjetoResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveRejeitarProjetoComNomeVazio() throws Exception {
        ProjetoRequest request = new ProjetoRequest(
            "",
            "Descrição válida",
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<ProjetoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl() + "/projetos", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Nome é obrigatório"));
    }

    @Test
    void deveRejeitarProjetoComNomeNulo() throws Exception {
        ProjetoRequest request = new ProjetoRequest(
            null,
            "Descrição válida",
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<ProjetoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl() + "/projetos", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Nome é obrigatório"));
    }

    @Test
    void deveRejeitarProjetoComNomeApenasEspacos() throws Exception {
        ProjetoRequest request = new ProjetoRequest(
            "   ",
            "Descrição válida",
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<ProjetoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl() + "/projetos", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Nome é obrigatório"));
    }

    @Test
    void deveAceitarProjetoComNomeValido() throws Exception {
        ProjetoRequest request = new ProjetoRequest(
            "Projeto de Teste",
            "Descrição do projeto de teste",
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<ProjetoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ProjetoResponse> response = restTemplate.postForEntity(
            getBaseUrl() + "/projetos", entity, ProjetoResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Projeto de Teste", response.getBody().nome());
        assertEquals("Descrição do projeto de teste", response.getBody().descricao());
    }

    @Test
    void deveBuscarProjetoPorId() throws Exception {
        // Primeiro cria um projeto
        ProjetoRequest request = new ProjetoRequest(
            "Projeto para Busca",
            "Projeto criado para teste de busca",
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<ProjetoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ProjetoResponse> createResponse = restTemplate.postForEntity(
            getBaseUrl() + "/projetos", entity, ProjetoResponse.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        Long projetoId = createResponse.getBody().id();

        // Agora busca o projeto pelo ID
        ResponseEntity<ProjetoResponse> getResponse = restTemplate.getForEntity(
            getBaseUrl() + "/projetos/" + projetoId, ProjetoResponse.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(projetoId, getResponse.getBody().id());
        assertEquals("Projeto para Busca", getResponse.getBody().nome());
        assertEquals("Projeto criado para teste de busca", getResponse.getBody().descricao());
    }

    @Test
    void deveRetornar404ParaProjetoInexistente() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            getBaseUrl() + "/projetos/99999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Projeto não encontrado"));
    }
}
