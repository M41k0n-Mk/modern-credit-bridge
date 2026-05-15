package com.modernbank.credit.domain.usecase;

import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.model.PropostaStatus;
import com.modernbank.credit.domain.valueobject.Cpf;
import com.modernbank.credit.domain.valueobject.Dinheiro;
import com.modernbank.credit.domain.repository.PropostaRepository;
import com.modernbank.credit.domain.service.ClienteHistoricoService;
import com.modernbank.credit.domain.service.RiscoCliente;
import com.modernbank.credit.domain.sqs.PropostaNotifier;
import com.modernbank.credit.domain.event.PropostaCriadaEvent;
import com.modernbank.credit.domain.factory.PropostaFactory;
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
            throw new NullPointerException("Repository não pode ser nulo");
        }
        if (clienteHistoricoService == null) {
            throw new NullPointerException("ClienteHistoricoService não pode ser nulo");
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
            throw new NullPointerException("Proposta não pode ser nula");
        }

        // 1) Consulta histórico/risco do cliente (ex.: Mainframe/Neptune)
        RiscoCliente risco = clienteHistoricoService.avaliarRisco(
                proposta.getCpf().getValor(), proposta.getValor().getValor());
        if (log.isDebugEnabled()) {
            String rawCpf = proposta.getCpf() != null ? proposta.getCpf().getValor() : null;
            String cpfMasked = rawCpf != null && rawCpf.length() >= 3
                    ? "***" + rawCpf.substring(rawCpf.length() - 3)
                    : "(indefinido)";
            log.debug("[CriarPropostaUseCase] Avaliação de risco concluída. cpf={}, valor={}, risco={}",
                    cpfMasked, proposta.getValor().getValor(), risco);
        }

        // 2) Define status inicial conforme risco
        PropostaStatus status;
        switch (risco) {
            case ALTO:
                status = PropostaStatus.REJEITADA; // prevenção de fraude/crédito
                break;
            case MEDIO:
                status = PropostaStatus.PENDENTE_REVISAO; // fila de análise manual
                break;
            default:
                status = PropostaStatus.PENDENTE; // fluxo normal
        }
        if (log.isInfoEnabled()) {
            log.info("[CriarPropostaUseCase] Status inicial definido: {}", status);
        }

        // 3) Recria a proposta com o status calculado (entidade imutável)
        // Boa prática: preferir Factory para criação/reconstrução do Aggregate
        Proposta propostaParaSalvar = PropostaFactory.reconstruir(
                proposta.getId(),
                proposta.getCpf(),
                proposta.getValor(),
                status
        );

        Proposta salva = repository.salvar(propostaParaSalvar);

        // 5) Publica Domain Event e, se aplicável, encaminha para processamento assíncrono
        propostaNotifier.publicar(new PropostaCriadaEvent(salva));
        if (salva.getStatus() != PropostaStatus.REJEITADA) {
            if (log.isDebugEnabled()) {
                log.debug("[CriarPropostaUseCase] Enfileirando proposta para processamento assíncrono. id={}", salva.getId());
            }
            // Mantemos compatibilidade com integrações que possam observar a fila
            // de propostas criadas através do mesmo notificador.
        }

        return salva;
    }
}