package com.modernbank.credit.sharedkernel.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * Value Object que representa um montante monetário.
 */
public record Dinheiro(BigDecimal valor, Currency moeda) {
    public Dinheiro(BigDecimal valor) {
        this(valor, Currency.getInstance("BRL"));
    }

    public Dinheiro {
        if (valor == null) {
            throw new NullPointerException("Valor não pode ser nulo");
        }
        if (moeda == null) {
            throw new NullPointerException("Moeda não pode ser nula");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
        valor = valor.setScale(2, RoundingMode.HALF_UP);
    }

    // Métodos auxiliares para compatibilidade com getX()
    public BigDecimal getValor() { return valor; }
    public Currency getMoeda() { return moeda; }

    @Override
    public String toString() {
        return valor + " " + moeda.getCurrencyCode();
    }
}

