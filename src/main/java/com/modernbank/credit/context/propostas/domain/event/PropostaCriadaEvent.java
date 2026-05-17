package com.modernbank.credit.context.propostas.domain.event;

import com.modernbank.credit.context.propostas.domain.model.Proposta;
import com.modernbank.credit.context.propostas.domain.model.PropostaStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class PropostaCriadaEvent implements DomainEvent {
  private final UUID propostaId;
  private final String cpf;
  private final BigDecimal valor;
  private final PropostaStatus status;
  private final Instant occurredAt;

  public PropostaCriadaEvent(Proposta proposta) {
    this.propostaId = proposta.getId();
    this.cpf = proposta.getCpf().getValor();
    this.valor = proposta.getValor().getValor();
    this.status = proposta.getStatus();
    this.occurredAt = Instant.now();
  }

  @Override
  public UUID aggregateId() {
    return propostaId;
  }

  @Override
  public String type() {
    return "PropostaCriada";
  }

  @Override
  public Instant occurredAt() {
    return occurredAt;
  }

  public UUID getPropostaId() {
    return propostaId;
  }

  public String getCpf() {
    return cpf;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public PropostaStatus getStatus() {
    return status;
  }
}
