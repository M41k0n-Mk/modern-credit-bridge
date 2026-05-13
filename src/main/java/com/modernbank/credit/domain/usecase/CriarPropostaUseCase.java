package com.modernbank.credit.domain.usecase;

import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.repository.PropostaRepository;
import com.modernbank.credit.domain.service.ClienteHistoricoService;
import com.modernbank.credit.domain.service.RiscoCliente;
import java.math.BigDecimal;
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
    private final ClienteHistoricoService clienteHistoricoService;

    /**
     * Construtor com injeção de dependência.
     *
     * @param repository repositório de propostas (não nulo)
     * @throws IllegalArgumentException se repository for nulo
     */
    public CriarPropostaUseCase(PropostaRepository repository, ClienteHistoricoService clienteHistoricoService) {
        this.repository = Objects.requireNonNull(repository, "Repository não pode ser nulo");
        this.clienteHistoricoService = Objects.requireNonNull(clienteHistoricoService, "ClienteHistoricoService não pode ser nulo");
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

        // 1) Consulta histórico/risco do cliente (ex.: Mainframe/Neptune)
        RiscoCliente risco = clienteHistoricoService.avaliarRisco(proposta.getCpf(), proposta.getValor());

        // 2) Define status inicial conforme risco
        String status;
        switch (risco) {
            case ALTO:
                status = "REJEITADA"; // prevenção de fraude/crédito
                break;
            case MEDIO:
                status = "PENDENTE_REVISAO"; // fila de análise manual
                break;
            default:
                status = "PENDENTE"; // fluxo normal
        }

        // 3) Recria a proposta com o status calculado (entidade imutável)
        Proposta propostaParaSalvar = new Proposta(
                proposta.getId(),
                proposta.getCpf(),
                proposta.getValor(),
                status
        );

        // 4) Persiste
        return repository.salvar(propostaParaSalvar);
    }
}
