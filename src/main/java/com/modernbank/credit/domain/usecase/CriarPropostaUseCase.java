package com.modernbank.credit.domain.usecase;

import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.repository.PropostaRepository;
import java.util.Objects;

/**
 * Use Case responsável pela criação de uma nova proposta de crédito.
 * Orquestra a lógica de negócio necessária para criar uma proposta.
 * 
 * PADRÃO: Use Case / Interactor (Clean Architecture)
 * Responsabilidades:
 * 1. Validação (delega ao entity se for simples)
 * 2. Coordenação entre agregados/services
 * 3. Persistência dos dados
 * 
 * @author ModernBank
 */
public class CriarPropostaUseCase {
    private final PropostaRepository repository;

    /**
     * Construtor com injeção de dependência.
     *
     * @param repository repositório de propostas (não nulo)
     * @throws IllegalArgumentException se repository for nulo
     */
    public CriarPropostaUseCase(PropostaRepository repository) {
        this.repository = Objects.requireNonNull(repository, "Repository não pode ser nulo");
    }

    /**
     * Executa a criação de uma nova proposta.
     * Fluxo:
     * 1. Valida a proposta (já feito no construtor da Proposta)
     * 2. Poderia chamar um DomainService se a lógica fosse complexa
     * 3. Persiste no repositório
     *
     * @param proposta proposta a ser criada (não nula)
     * @return proposta salva com ID e status preenchidos
     * @throws IllegalArgumentException se proposta for nula
     * @throws IllegalArgumentException se proposta for inválida
     */
    public Proposta executar(Proposta proposta) {
        Objects.requireNonNull(proposta, "Proposta não pode ser nula");
        
        // Aqui poderiam estar validações e regras de negócio mais complexas
        // Ex: chamar um DomainService, validar regras de crédito, etc.
        
        return repository.salvar(proposta);
    }
}
