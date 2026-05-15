package com.modernbank.credit.domain.sqs;

import com.modernbank.credit.domain.event.DomainEvent;

/**
 * Porta de saída do domínio para publicação de eventos em mensageria.
 */
public interface PropostaNotifier {
    void publicar(DomainEvent event);
}
