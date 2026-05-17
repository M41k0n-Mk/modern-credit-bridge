package com.modernbank.credit.context.propostas.domain.sqs;

import com.modernbank.credit.context.propostas.domain.event.DomainEvent;

/** Porta de saída do domínio para publicação de eventos em mensageria. */
public interface PropostaNotifier {
  void publicar(DomainEvent event);
}
