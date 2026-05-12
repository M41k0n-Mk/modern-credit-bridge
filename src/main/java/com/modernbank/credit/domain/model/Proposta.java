package com.modernbank.credit.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio que representa uma Proposta de Crédito.
 * Encapsula regras de negócio e validações específicas.
 */
public class Proposta {
    private final UUID id;
    private final String cpf;
    private final BigDecimal valor;
    private final String status;

    /**
     * Construtor privado para garantir validação via factory method ou construtor público com validação.
     */
    public Proposta(String cpf, BigDecimal valor) {
        this(UUID.randomUUID(), cpf, valor, "PENDENTE");
    }

    /**
     * Construtor completo para reconstrução de estado persistido.
     */
    public Proposta(UUID id, String cpf, BigDecimal valor, String status) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.cpf = Objects.requireNonNull(cpf, "CPF não pode ser nulo");
        this.valor = Objects.requireNonNull(valor, "Valor não pode ser nulo");
        this.status = Objects.requireNonNull(status, "Status não pode ser nulo");

        validar();
    }

    /**
     * Validações de regra de negócio do domínio.
     */
    private void validar() {
        if (cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
    }

    // Getters imutáveis
    public UUID getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposta proposta = (Proposta) o;
        return Objects.equals(id, proposta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Proposta{" +
                "id=" + id +
                ", cpf='" + cpf + '\'' +
                ", valor=" + valor +
                ", status='" + status + '\'' +
                '}';
    }
}
