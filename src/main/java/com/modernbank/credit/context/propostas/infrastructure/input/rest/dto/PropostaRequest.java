package com.modernbank.credit.context.propostas.infrastructure.input.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para requisição de criação de proposta. Representa os dados recebidos da API REST com
 * validações.
 *
 * <p>Anotações Lombok: - @Getter: Gera getters para todos os campos - @Setter: Gera setters para
 * todos os campos - @NoArgsConstructor: Gera construtor sem argumentos - @AllArgsConstructor: Gera
 * construtor com todos os campos
 *
 * <p>Anotações Jakarta Validation: - @NotBlank: Valida que a string não é nula/vazia - @DecimalMin:
 * Valida o valor mínimo de um BigDecimal
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropostaRequest {

  @NotBlank(message = "CPF não pode ser vazio")
  private String cpf;

  @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
  private BigDecimal valor;
}
