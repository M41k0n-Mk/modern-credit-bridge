package com.modernbank.credit.context.propostas.infrastructure.input.rest.mapper;

import com.modernbank.credit.context.propostas.infrastructure.input.rest.dto.PropostaRequest;
import com.modernbank.credit.context.propostas.infrastructure.input.rest.dto.PropostaResponse;
import com.modernbank.credit.context.propostas.domain.model.Proposta;
import com.modernbank.credit.context.propostas.domain.factory.PropostaFactory;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper com MapStruct para conversão entre DTOs e Entidades de Domínio.
 * MapStruct automatiza a geração de código de conversão em tempo de compilação.
 * 
 * Benefícios:
 * - Performance: Conversão feita em compile-time (sem reflexão)
 * - Type-safe: Verificação de tipos em tempo de compilação
 * - Menos boilerplate: Não precisa escrever getters/setters manualmente
 * - Facilmente extensível: Dapat adicionar custom mappings
 *
 */
@Mapper(componentModel = "spring")
public interface PropostaMapperInterface {

    PropostaMapperInterface INSTANCE = Mappers.getMapper(PropostaMapperInterface.class);

    /**
     * Converte um DTO de requisição para uma entidade de domínio.
     *
     * @param request dados de entrada da API (não nulo)
     * @return entidade Proposta do domínio
     */
    default Proposta toDomain(PropostaRequest request) {
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
    default PropostaResponse toResponse(Proposta proposta) {
        if (proposta == null) {
            throw new NullPointerException("Proposta não pode ser nula");
        }
        return new PropostaResponse(proposta.getId(), proposta.getStatus().name());
    }
}
