package com.infrastructure.output.persistence.entity.repository;

import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.exception.PropostaNotFoundException;
import com.modernbank.credit.domain.repository.PropostaRepository;
import com.infrastructure.output.persistence.entity.PropostaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementação do repositório de Propostas usando Spring Data JPA.
 * Adapta a interface de domínio para a implementação de persistência.
 * 
 * Padrão: Repository Pattern + Adapter Pattern
 * - Implementa o contrato definido em PropostaRepository
 * - Encapsula a lógica de persistência com JPA
 * - Permite trocar implementação sem afetar o domínio
 * 
 * Anotação @Repository:
 * - Identifica como componente de acesso a dados do Spring
 * - Ativa tratamento automático de exceções JPA
 * 
 * @author ModernBank
 */
@Repository
@RequiredArgsConstructor
public class PropostaRepositoryImpl implements PropostaRepository {

    private final PropostaRepositoryJpa jpaRepository;

    /**
     * Salva uma proposta no banco de dados.
     * Converte da entidade de domínio para JPA e vice-versa.
     *
     * @param proposta proposta a ser salva (não nula)
     * @return proposta salva no banco de dados
     * @throws IllegalArgumentException se proposta for nula
     */
    @Override
    public Proposta salvar(Proposta proposta) {
        if (proposta == null) {
            throw new IllegalArgumentException("Proposta não pode ser nula");
        }
        
        PropostaEntity entity = PropostaEntity.fromDomain(proposta);
        PropostaEntity saved = jpaRepository.save(entity);
        
        return saved.toDomain();
    }

    /**
     * Busca uma proposta pelo ID.
     * Lança exceção se não encontrado (opcional).
     *
     * @param id identificador único da proposta (não nulo)
     * @return Optional contendo a proposta se encontrada
     * @throws IllegalArgumentException se id for nulo
     */
    @Override
    public Optional<Proposta> buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        
        return jpaRepository.findById(id)
                .map(PropostaEntity::toDomain);
    }

    /**
     * Busca uma proposta pelo ID e lança exceção se não encontrada.
     * Util para quando a proposta DEVE existir.
     *
     * @param id identificador único
     * @return proposta encontrada
     * @throws PropostaNotFoundException se não encontrada
     */
    public Proposta buscarPorIdOuLancarExcecao(UUID id) {
        return buscarPorId(id)
                .orElseThrow(() -> new PropostaNotFoundException(
                        "Proposta com ID " + id + " não encontrada"
                ));
    }
}
