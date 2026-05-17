package com.modernbank.credit.context.propostas.infrastructure.input.rest;

import com.modernbank.credit.context.propostas.domain.model.Proposta;
import com.modernbank.credit.context.propostas.domain.usecase.CriarPropostaUseCase;
import com.modernbank.credit.context.propostas.infrastructure.input.rest.dto.PropostaRequest;
import com.modernbank.credit.context.propostas.infrastructure.input.rest.dto.PropostaResponse;
import com.modernbank.credit.context.propostas.infrastructure.input.rest.mapper.PropostaMapper;
import jakarta.validation.Valid;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável por gerenciar as propostas de crédito. Expõe endpoints REST para operações
 * com propostas.
 */
@RestController
@RequestMapping("/v1/propostas")
@Slf4j
public class PropostaController {

  private final CriarPropostaUseCase criarPropostaUseCase;

  /**
   * Construtor com injeção de dependência via construtor. Boa prática: permite fácil testabilidade
   * e explicitação de dependências.
   *
   * @param criarPropostaUseCase use case responsável por criar propostas
   */
  public PropostaController(CriarPropostaUseCase criarPropostaUseCase) {
    this.criarPropostaUseCase = criarPropostaUseCase;
  }

  /**
   * Endpoint POST para criar uma nova proposta de crédito. Orquestra o fluxo: DTO -> Domínio -> Use
   * Case -> Domínio -> DTO Response.
   *
   * @param request dados da proposta a ser criada
   * @return ResponseEntity com os dados da proposta criada
   */
  @PostMapping
  public ResponseEntity<PropostaResponse> criar(@Valid @RequestBody PropostaRequest request) {
    long start = System.currentTimeMillis();
    if (log.isInfoEnabled()) {
      String cpfMasked =
          request.getCpf() != null && request.getCpf().length() >= 3
              ? "***" + request.getCpf().substring(request.getCpf().length() - 3)
              : "(indefinido)";
      log.info(
          "[PropostaController] Recebida solicitação de criação de proposta. cpf={}, valor={} ts={}",
          cpfMasked,
          request.getValor(),
          Instant.now());
    }

    if (log.isDebugEnabled()) {
      log.debug("Payload recebido: {}", request);
    }

    // 1. Converte Request (DTO) para Domínio
    Proposta propostaDomain = PropostaMapper.toDomain(request);

    // 2. Executa Use Case (lógica de negócio)
    Proposta resultado = criarPropostaUseCase.executar(propostaDomain);

    // 3. Converte Domínio para Response (DTO)
    PropostaResponse response = PropostaMapper.toResponse(resultado);

    if (log.isInfoEnabled()) {
      long took = System.currentTimeMillis() - start;
      log.info(
          "[PropostaController] Proposta criada com sucesso. id={}, status={}, tookMs={}",
          response.getId(),
          response.getStatus(),
          took);
    }

    // 4. Retorna com status HTTP 201 Created (mais apropriado para POST)
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
