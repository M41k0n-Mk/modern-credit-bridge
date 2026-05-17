package com.modernbank.credit.context.propostas.domain.factory;

import com.modernbank.credit.context.propostas.domain.model.Proposta;
import com.modernbank.credit.context.propostas.domain.model.PropostaStatus;
import com.modernbank.credit.sharedkernel.vo.Cpf;
import com.modernbank.credit.sharedkernel.vo.Dinheiro;
import java.util.UUID;

/** Domain Factory para criação/reconstrução de Propostas mantendo invariantes. */
public final class PropostaFactory {

  private PropostaFactory() {}

  // Criação de novas propostas (nome claro: construir)
  public static Proposta construir(String cpf, java.math.BigDecimal valor) {
    return new Proposta(new Cpf(cpf), new Dinheiro(valor));
  }

  public static Proposta construir(Cpf cpf, Dinheiro valor) {
    return new Proposta(cpf, valor);
  }

  // Reconstrução: para reidratar a partir do armazenamento (ex.: JPA/Event Sourcing)
  public static Proposta reconstruir(UUID id, Cpf cpf, Dinheiro valor, PropostaStatus status) {
    return new Proposta(id, cpf, valor, status);
  }
}
