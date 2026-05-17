package com.modernbank.credit.context.propostas.infrastructure.input.rest.mapper;

import com.modernbank.credit.context.propostas.domain.factory.PropostaFactory;
import com.modernbank.credit.context.propostas.domain.model.Proposta;
import com.modernbank.credit.context.propostas.infrastructure.input.rest.dto.PropostaRequest;
import com.modernbank.credit.context.propostas.infrastructure.input.rest.dto.PropostaResponse;

/**
 * Mapper responsável pela conversão entre objetos de DTO e Domínio. Mapeia dados da camada de
 * entrada (API) para o domínio e vice-versa.
 *
 * <p>PADRÃO: Mapper / Converter (Anti-Corruption Layer) Objetivo: Isolar as mudanças entre camadas,
 * facilitando manutenção.
 *
 * <p>Nota: Poderia usar MapStruct para automatizar essas conversões em projetos maiores.
 *
 * @author ModernBank
 */
public class PropostaMapper {

  /**
   * Converte um DTO de requisição para uma entidade de domínio.
   *
   * @param request dados de entrada da API (não nulo)
   * @return entidade Proposta do domínio
   */
  public static Proposta toDomain(PropostaRequest request) {
    if (request == null) {
      throw new NullPointerException("PropostaRequest não pode ser nula");
    }
    return PropostaFactory.construir(request.getCpf(), request.getValor());
  }

  /**
   * Converte uma entidade de domínio para um DTO de resposta.
   *
   * @param proposta entidade de domínio (não nula)
   * @return DTO com os dados para retorno da API
   */
  public static PropostaResponse toResponse(Proposta proposta) {
    if (proposta == null) {
      throw new NullPointerException("Proposta não pode ser nula");
    }
    return new PropostaResponse(proposta.getId(), proposta.getStatus().name());
  }

  /** Construtor privado para evitar instanciação. Classe utilitária com métodos estáticos. */
  private PropostaMapper() {
    throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada");
  }
}
