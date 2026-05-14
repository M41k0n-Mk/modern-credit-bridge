package com.modernbank.credit.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO para resposta de criação de proposta.
 * Representa os dados retornados pela API REST.
 * 
 * Anotações Lombok:
 * - @Getter: Gera getters para todos os campos
 * - @Setter: Gera setters para todos os campos
 * - @NoArgsConstructor: Gera construtor sem argumentos
 * - @AllArgsConstructor: Gera construtor com todos os campos
 * 
 * Contém apenas os campos necessários para a resposta, seguindo o princípio
 * de mínima exposição de dados (menos é mais).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropostaResponse {
    private UUID id;
    private String status;
}
