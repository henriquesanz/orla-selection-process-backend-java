package com.corelyon.mvp.integration;

import com.corelyon.mvp.app.dto.FuncionarioRequest;
import com.corelyon.mvp.app.dto.FuncionarioResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class FuncionarioControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @Test
    void deveCriarFuncionarioComSucesso() throws Exception {
        FuncionarioRequest request = new FuncionarioRequest(
            "João Silva",
            "11144477735",
            "joao@email.com",
            5000.0,
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<FuncionarioRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<FuncionarioResponse> response = restTemplate.postForEntity(
            getBaseUrl() + "/funcionarios", entity, FuncionarioResponse.class);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().nome());
        assertEquals("11144477735", response.getBody().cpf());
        assertEquals("joao@email.com", response.getBody().email());
        assertEquals(5000.0, response.getBody().salario());
    }

    @Test
    void deveListarFuncionarios() {
        ResponseEntity<FuncionarioResponse[]> response = restTemplate.getForEntity(
            getBaseUrl() + "/funcionarios", FuncionarioResponse[].class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void deveRejeitarCPFInvalido() throws Exception {
        FuncionarioRequest request = new FuncionarioRequest(
            "João Silva",
            "12345678900",
            "joao@email.com",
            5000.0,
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<FuncionarioRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl() + "/funcionarios", entity, String.class);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("CPF inválido"));
    }

    @Test
    void deveRejeitarCPFComTodosDigitosIguais() throws Exception {
        FuncionarioRequest request = new FuncionarioRequest(
            "João Silva",
            "11111111111",
            "joao@email.com",
            5000.0,
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<FuncionarioRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            getBaseUrl() + "/funcionarios", entity, String.class);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("CPF inválido"));
    }

    @Test
    void deveAceitarCPFValido() throws Exception {
        FuncionarioRequest request = new FuncionarioRequest(
            "Maria Santos",
            "12345678909",
            "maria@email.com",
            6000.0,
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<FuncionarioRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<FuncionarioResponse> response = restTemplate.postForEntity(
            getBaseUrl() + "/funcionarios", entity, FuncionarioResponse.class);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("12345678909", response.getBody().cpf());
    }
}
