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
        // Arrange
        FuncionarioRequest request = new FuncionarioRequest(
            "João Silva",
            "12345678901",
            "joao@email.com",
            5000.0,
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<FuncionarioRequest> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<FuncionarioResponse> response = restTemplate.postForEntity(
            getBaseUrl() + "/funcionarios", entity, FuncionarioResponse.class);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("João Silva", response.getBody().nome());
        assertEquals("12345678901", response.getBody().cpf());
        assertEquals("joao@email.com", response.getBody().email());
        assertEquals(5000.0, response.getBody().salario());
    }

    @Test
    void deveListarFuncionarios() {
        // Act
        ResponseEntity<FuncionarioResponse[]> response = restTemplate.getForEntity(
            getBaseUrl() + "/funcionarios", FuncionarioResponse[].class);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
