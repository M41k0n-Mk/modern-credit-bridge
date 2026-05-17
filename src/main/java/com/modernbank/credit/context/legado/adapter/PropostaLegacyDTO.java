package com.modernbank.credit.context.legado.adapter;

import java.util.Objects;

/**
 * DTO que representa um registro do mainframe (formato legado). Exemplo de linha:
 * "0001|12345678900|00000010000|20260515|S" campos:
 * idLegado|cpf|valorEmCentavos|dataAAAAmmdd|statusCodigo
 */
public final class PropostaLegacyDTO {
  private final String idLegado;
  private final String cpf;
  private final String valorEmCentavos;
  private final String data;
  private final String statusCodigo;

  private PropostaLegacyDTO(
      String idLegado, String cpf, String valorEmCentavos, String data, String statusCodigo) {
    this.idLegado = idLegado;
    this.cpf = cpf;
    this.valorEmCentavos = valorEmCentavos;
    this.data = data;
    this.statusCodigo = statusCodigo;
  }

  public static PropostaLegacyDTO fromLine(String line) {
    Objects.requireNonNull(line, "Linha legado não pode ser nula");
    // Suporta pipe-delimited simples; trimming defensivo
    String[] parts = line.split("\\|");
    if (parts.length < 5) {
      throw new IllegalArgumentException(
          "Linha legado inválida: esperava 5 campos, recebeu=" + parts.length);
    }
    return new PropostaLegacyDTO(
        parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
  }

  public String getIdLegado() {
    return idLegado;
  }

  public String getCpf() {
    return cpf;
  }

  public String getValorEmCentavos() {
    return valorEmCentavos;
  }

  public String getData() {
    return data;
  }

  public String getStatusCodigo() {
    return statusCodigo;
  }

  @Override
  public String toString() {
    return "PropostaLegacyDTO{"
        + "idLegado='"
        + idLegado
        + '\''
        + ", cpf='"
        + cpf
        + '\''
        + ", valorEmCentavos='"
        + valorEmCentavos
        + '\''
        + ", data='"
        + data
        + '\''
        + ", statusCodigo='"
        + statusCodigo
        + '\''
        + '}';
  }
}
