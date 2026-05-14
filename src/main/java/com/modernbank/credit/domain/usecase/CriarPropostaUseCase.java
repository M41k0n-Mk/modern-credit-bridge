package com.modernbank.credit.domain.usecase;

import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.repository.PropostaRepository;
import com.modernbank.credit.domain.service.ClienteHistoricoService;
import com.modernbank.credit.domain.service.RiscoCliente;
import com.modernbank.credit.domain.sqs.PropostaNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 */
public class CriarPropostaUseCase {

    private final PropostaRepository repository;
    private final ClienteHistoricoService clienteHistoricoService;
    private final PropostaNotifier propostaNotifier;
    private static final Logger log = LoggerFactory.getLogger(CriarPropostaUseCase.class);

    /**
     * Construtor com injeção de dependência.
     *
     * @param repository repositório de propostas (não nulo)
     * @throws IllegalArgumentException se repository for nulo
     */
    public CriarPropostaUseCase(PropostaRepository repository, ClienteHistoricoService clienteHistoricoService,
                                PropostaNotifier propostaNotifier) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository não pode ser nulo");
        }
        if (clienteHistoricoService == null) {
            throw new IllegalArgumentException("ClienteHistoricoService não pode ser nulo");
        }
        this.repository = repository;
        this.clienteHistoricoService = clienteHistoricoService;
        this.propostaNotifier = propostaNotifier;
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
        if (proposta == null) {
            throw new IllegalArgumentException("Proposta não pode ser nula");
        }

        // 1) Consulta histórico/risco do cliente (ex.: Mainframe/Neptune)
        RiscoCliente risco = clienteHistoricoService.avaliarRisco(proposta.getCpf(), proposta.getValor());
        if (log.isDebugEnabled()) {
            String cpfMasked = proposta.getCpf() != null && proposta.getCpf().length() >= 3
                    ? "***" + proposta.getCpf().substring(proposta.getCpf().length() - 3)
                    : "(indefinido)";
            log.debug("[CriarPropostaUseCase] Avaliação de risco concluída. cpf={}, valor={}, risco={}",
                    cpfMasked, proposta.getValor(), risco);
        }

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
        if (log.isInfoEnabled()) {
            log.info("[CriarPropostaUseCase] Status inicial definido: {}", status);
        }

        // 3) Recria a proposta com o status calculado (entidade imutável)
        Proposta propostaParaSalvar = new Proposta(
                proposta.getId(),
                proposta.getCpf(),
                proposta.getValor(),
                status
        );

        Proposta salva = repository.salvar(propostaParaSalvar);

        // 5) Se for um fluxo que exige processamento assíncrono (ex: tudo que não foi rejeitado)
        if (!"REJEITADA".equals(salva.getStatus())) {
            if (log.isDebugEnabled()) {
                log.debug("[CriarPropostaUseCase] Enfileirando proposta para processamento assíncrono. id={}", salva.getId());
            }
            propostaNotifier.notificarCriacao(salva);
        }

        return salva;
    }
}