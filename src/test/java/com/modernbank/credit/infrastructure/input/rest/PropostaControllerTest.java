package com.modernbank.credit.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernbank.credit.infrastructure.input.rest.dto.PropostaRequest;
import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.usecase.CriarPropostaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para PropostaController.
 * 
 * Testa o endpoint REST utilizando MockMvc para simular requisições HTTP
 * sem iniciar o servidor.
 * 
 * @author ModernBank
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes - PropostaController")
class PropostaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CriarPropostaUseCase criarPropostaUseCaseMock;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new PropostaController(criarPropostaUseCaseMock))
                .build();
    }

    @Test
    @DisplayName("Deve criar proposta e retornar 201 Created")
    void deveCriarPropostaRetornando201() throws Exception {
        // Arrange
        String cpf = "12345678900";
        BigDecimal valor = new BigDecimal("1000.00");
        UUID idGerado = UUID.randomUUID();

        PropostaRequest request = new PropostaRequest(cpf, valor);
        Proposta propostaSalva = new Proposta(idGerado, cpf, valor, "PENDENTE");

        when(criarPropostaUseCaseMock.executar(any(Proposta.class)))
                .thenReturn(propostaSalva);

        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.status", equalTo("PENDENTE")));
    }

    @Test
    @DisplayName("Deve validar CPF obrigatório na requisição")
    void deveValidarCpfObrigatorio() throws Exception {
        // Arrange
        PropostaRequest requestSemCpf = new PropostaRequest();
        requestSemCpf.setValor(new BigDecimal("1000.00"));
        // CPF agora é null/blank

        String requestJson = objectMapper.writeValueAsString(requestSemCpf);

        // Act & Assert
        mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve validar valor mínimo na requisição")
    void deveValidarValorMinimo() throws Exception {
        // Arrange
        PropostaRequest requestComValorInvalido = new PropostaRequest();
        requestComValorInvalido.setCpf("12345678900");
        requestComValorInvalido.setValor(new BigDecimal("0.00"));

        String requestJson = objectMapper.writeValueAsString(requestComValorInvalido);

        // Act & Assert
        mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar resposta com ID e status")
    void deveRetornarRespostaComIdEStatus() throws Exception {
        // Arrange
        String cpf = "12345678900";
        BigDecimal valor = new BigDecimal("1000.00");
        UUID idGerado = UUID.randomUUID();

        PropostaRequest request = new PropostaRequest(cpf, valor);
        Proposta propostaSalva = new Proposta(idGerado, cpf, valor, "PENDENTE");

        when(criarPropostaUseCaseMock.executar(any(Proposta.class)))
                .thenReturn(propostaSalva);

        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.status", equalTo("PENDENTE")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request para JSON inválido")
    void deveRetornarBadRequestParaJsonInvalido() throws Exception {
        // Arrange
        String jsonInvalido = "{ json inválido }";

        // Act & Assert
        mockMvc.perform(post("/v1/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }
}
