package com.modernbank.credit.domain.repository;

import com.modernbank.credit.domain.model.Proposta;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface do repositório de Propostas.
 * Define o contrato para operações de persistência de propostas.
 * Implementação: responsabilidade da camada de infraestrutura (Spring Data JPA).
 * 
 * PADRÃO: Repository Pattern (Domain-Driven Design)
 * Objetivo: Isolar a lógica de acesso a dados da camada de negócio.
 */
public interface PropostaRepository {

    /**
     * Salva uma proposta no banco de dados.
     * Se já existe, atualiza; caso contrário, insere.
     *
     * @param proposta proposta a ser salva (não nula)
     * @return proposta salva com ID preenchido
     * @throws IllegalArgumentException se proposta for nula
     */
    Proposta salvar(Proposta proposta);

    /**
     * Busca uma proposta pelo ID.
     *
     * @param id identificador único da proposta (não nulo)
     * @return Optional contendo a proposta se encontrada
     */
    Optional<Proposta> buscarPorId(UUID id);
}