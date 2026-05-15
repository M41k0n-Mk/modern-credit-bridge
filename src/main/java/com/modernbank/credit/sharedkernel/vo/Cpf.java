package com.modernbank.credit.sharedkernel.vo;

/**
 * Value Object que representa um CPF não formatado (11 dígitos).
 */
public record Cpf(String valor) {
    public Cpf {
        if (valor == null) {
            throw new NullPointerException("CPF não pode ser nulo");
        }
        String digits = valor.replaceAll("\\D", "");
        if (digits.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos");
        }
        valor = digits;
    }
    // Método auxiliar para compatibilidade com getX()
    public String getValor() { return valor; }
    @Override
    public String toString() {
        return valor;
    }
}

