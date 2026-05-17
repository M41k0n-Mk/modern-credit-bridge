package com.modernbank.credit.context.legado.adapter;

import java.util.List;

/**
 * Cliente simulado do mainframe — em um cenário real seria um leitor de arquivos ou integrador.
 * Aqui fornece algumas linhas de exemplo para demonstração.
 */
public final class LegadoMainframeClient {

  private LegadoMainframeClient() {}

  public static List<String> lerLinhasExemplo() {
    return List.of(
        // id|cpf|valorCentavos|data|status
        "0001|12345678900|00000010000|20260515|S",
        "0002|98765432100|00000025000|20260514|A",
        "0003|11122233344|00000005000|20260513|R");
  }
}
