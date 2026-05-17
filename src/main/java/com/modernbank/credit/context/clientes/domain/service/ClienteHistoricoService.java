package com.modernbank.credit.context.clientes.domain.service;

import java.math.BigDecimal;

/**
 * Domain Service responsável por consultar histórico do cliente em sistemas legados (Mainframe) ou
 * bancos de grafos (fraude), retornando um nível de risco para a proposta.
 */
public interface ClienteHistoricoService {

  /**
   * Avalia o risco do cliente com base em seu CPF e, opcionalmente, no valor solicitado.
   * Implementações podem integrar com Neptune (grafo).
   *
   * @param cpf CPF do cliente (não nulo/sem formatação específica)
   * @param valor Valor solicitado na proposta
   * @return nível de risco do cliente
   */
  RiscoCliente avaliarRisco(String cpf, BigDecimal valor);
}
