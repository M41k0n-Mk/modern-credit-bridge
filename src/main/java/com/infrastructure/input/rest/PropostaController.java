package com.infrastructure.input.rest;

import com.infrastructure.input.rest.dto.PropostaRequest;
import com.infrastructure.input.rest.dto.PropostaResponse;
import com.infrastructure.input.rest.mapper.PropostaMapper;
import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.usecase.CriarPropostaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável por gerenciar as propostas de crédito.
 * Expõe endpoints REST para operações com propostas.
 */
@RestController
@RequestMapping("/v1/propostas")
public class PropostaController {

    private final CriarPropostaUseCase criarPropostaUseCase;

    /**
     * Construtor com injeção de dependência via construtor.
     * Boa prática: permite fácil testabilidade e explicitação de dependências.
     *
     * @param criarPropostaUseCase use case responsável por criar propostas
     */
    public PropostaController(CriarPropostaUseCase criarPropostaUseCase) {
        this.criarPropostaUseCase = criarPropostaUseCase;
    }

    /**
     * Endpoint POST para criar uma nova proposta de crédito.
     * Orquestra o fluxo: DTO -> Domínio -> Use Case -> Domínio -> DTO Response.
     *
     * @param request dados da proposta a ser criada
     * @return ResponseEntity com os dados da proposta criada
     */
    @PostMapping
    public ResponseEntity<PropostaResponse> criar(@Valid @RequestBody PropostaRequest request) {
        // 1. Converte Request (DTO) para Domínio
        Proposta propostaDomain = PropostaMapper.toDomain(request);

        // 2. Executa Use Case (lógica de negócio)
        Proposta resultado = criarPropostaUseCase.executar(propostaDomain);

        // 3. Converte Domínio para Response (DTO)
        PropostaResponse response = PropostaMapper.toResponse(resultado);

        // 4. Retorna com status HTTP 201 Created (mais apropriado para POST)
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
