package com.modernbank.credit.context.propostas.domain.model;

import com.modernbank.credit.sharedkernel.vo.Cpf;
import com.modernbank.credit.sharedkernel.vo.Dinheiro;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio que representa uma Proposta de Crédito.
 * Encapsula regras de negócio e validações específicas.
 */
public class Proposta {
    // Aggregate Root: Proposta
    private final UUID id;
    private final Cpf cpf;
    private final Dinheiro valor;
    private final PropostaStatus status;

    /**
     * Construtor privado para garantir validação via factory method ou construtor público com validação.
     */
    @Deprecated
    public Proposta(String cpf, java.math.BigDecimal valor) {
        this(new Cpf(cpf), new Dinheiro(valor));
    }

    @Deprecated
    public Proposta(Cpf cpf, Dinheiro valor) {
        this(UUID.randomUUID(), cpf, valor, PropostaStatus.PENDENTE);
    }

    /**
     * Construtor completo para reconstrução de estado persistido.
     */
    @Deprecated
    public Proposta(UUID id, Cpf cpf, Dinheiro valor, PropostaStatus status) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.cpf = Objects.requireNonNull(cpf, "CPF não pode ser nulo");
        this.valor = Objects.requireNonNull(valor, "Valor não pode ser nulo");
        this.status = Objects.requireNonNull(status, "Status não pode ser nulo");

        validar();
    }

    /**
     * Construtor de compatibilidade para testes e camadas antigas.
     * Evita quebra ao migrar para VOs e enum.
     */
    @Deprecated
    public Proposta(UUID id, String cpf, java.math.BigDecimal valor, String status) {
        this(id, new Cpf(cpf), new Dinheiro(valor), PropostaStatus.valueOf(status));
    }

    /**
     * Construtor de compatibilidade (UUID, Cpf, Dinheiro, String status) usado em testes legados.
     */
    @Deprecated
    public Proposta(UUID id, Cpf cpf, Dinheiro valor, String status) {
        this(id, cpf, valor, PropostaStatus.valueOf(status));
    }

    /**
     * Validações de regra de negócio do domínio.
     */
    private void validar() {
        // Invariantes cobertas pelos VOs (Cpf e Dinheiro)
    }

    // Getters imutáveis
    public UUID getId() {
        return id;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public Dinheiro getValor() {
        return valor;
    }

    public PropostaStatus getStatus() {
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
                ", cpf=" + cpf +
                ", valor=" + valor +
                ", status=" + status +
                '}';
    }
}
