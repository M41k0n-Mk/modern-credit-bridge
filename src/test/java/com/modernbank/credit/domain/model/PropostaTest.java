package com.modernbank.credit.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Proposta.
 * 
 * Testa validações de domínio, imutabilidade e comportamentos da entidade.
 * 
 * @author ModernBank
 */
@DisplayName("Testes - Proposta (Model)")
class PropostaTest {

    @Test
    @DisplayName("Deve criar proposta com construtor simples gerando UUID")
    void deveCriarPropostaComConstrutorSimples() {
        // Act
        Proposta proposta = new Proposta("12345678900", new BigDecimal("1000.00"));

        // Assert
        assertNotNull(proposta.getId());
        assertEquals("12345678900", proposta.getCpf());
        assertEquals(new BigDecimal("1000.00"), proposta.getValor());
        assertEquals("PENDENTE", proposta.getStatus());
    }

    @Test
    @DisplayName("Deve criar proposta com construtor completo")
    void deveCriarPropostaComConstrutorCompleto() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Proposta proposta = new Proposta(id, "12345678900", new BigDecimal("1000.00"), "APROVADA");

        // Assert
        assertEquals(id, proposta.getId());
        assertEquals("12345678900", proposta.getCpf());
        assertEquals(new BigDecimal("1000.00"), proposta.getValor());
        assertEquals("APROVADA", proposta.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF é nulo")
    void deveLancarExcecaoQuandoCpfNulo() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new Proposta(null, new BigDecimal("1000.00"));
        });
        // Assert
        assertEquals("CPF não pode ser nulo", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF é vazio")
    void deveLancarExcecaoQuandoCpfVazio() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Proposta("", new BigDecimal("1000.00"));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF é em branco")
    void deveLancarExcecaoQuandoCpfEmBranco() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Proposta("   ", new BigDecimal("1000.00"));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção quando Valor é nulo")
    void deveLancarExcecaoQuandoValorNulo() {
        // Act
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new Proposta("12345678900", null);
        });
        // Assert
        assertEquals("Valor não pode ser nulo", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando Valor é zero")
    void deveLancarExcecaoQuandoValorZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Proposta("12345678900", new BigDecimal("0"));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção quando Valor é negativo")
    void deveLancarExcecaoQuandoValorNegativo() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Proposta("12345678900", new BigDecimal("-100.00"));
        });
    }

    @Test
    @DisplayName("Deve ter ID diferente para duas propostas distintas")
    void deveGerarIdsDiferentesParaPropostasDiferentes() {
        // Act
        Proposta proposta1 = new Proposta("12345678900", new BigDecimal("1000.00"));
        Proposta proposta2 = new Proposta("98765432100", new BigDecimal("2000.00"));

        // Assert
        assertNotEquals(proposta1.getId(), proposta2.getId());
    }

    @Test
    @DisplayName("Deve comparar igualdade por ID")
    void deveCompararIgualdadePorId() {
        // Arrange
        UUID id = UUID.randomUUID();
        Proposta proposta1 = new Proposta(id, "12345678900", new BigDecimal("1000.00"), "PENDENTE");
        Proposta proposta2 = new Proposta(id, "98765432100", new BigDecimal("2000.00"), "APROVADA");

        // Act & Assert
        assertEquals(proposta1, proposta2);
    }

    @Test
    @DisplayName("Deve retornar false quando comparado com null")
    void deveRetornarFalseQuandoComparadoComNull() {
        // Arrange
        Proposta proposta = new Proposta("12345678900", new BigDecimal("1000.00"));

        // Act & Assert
        assertNotEquals(proposta, null);
    }

    @Test
    @DisplayName("Deve gerar hashCode consistente")
    void deveGerarHashCodeConsistente() {
        // Arrange
        UUID id = UUID.randomUUID();
        Proposta proposta1 = new Proposta(id, "12345678900", new BigDecimal("1000.00"), "PENDENTE");
        Proposta proposta2 = new Proposta(id, "98765432100", new BigDecimal("2000.00"), "APROVADA");

        // Act & Assert
        assertEquals(proposta1.hashCode(), proposta2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString com informações relevantes")
    void deveGerarToStringComInformacoesRelevantes() {
        // Arrange
        Proposta proposta = new Proposta("12345678900", new BigDecimal("1000.00"));

        // Act
        String toString = proposta.toString();

        // Assert
        assertTrue(toString.contains("Proposta"));
        assertTrue(toString.contains("12345678900"));
        assertTrue(toString.contains("1000"));
    }

    @Test
    @DisplayName("Deve ser imutável após criação")
    void deveSerImutavelAposCriacao() {
        // Arrange
        Proposta proposta = new Proposta("12345678900", new BigDecimal("1000.00"));

        // Assert - Getters retornam valores corretos
        assertNotNull(proposta.getId());
        assertEquals("12345678900", proposta.getCpf());
        assertEquals(new BigDecimal("1000.00"), proposta.getValor());
        assertEquals("PENDENTE", proposta.getStatus());

        // Não há setters públicos, então a proposta é imutável
    }
}
