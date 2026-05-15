package com.modernbank.credit.context.propostas.domain.exception;

/**
 * Exceção base para erros de domínio.
 * Todas as exceções relacionadas à lógica de negócio devem herdar desta classe.
 * 
 * Benefícios:
 * - Facilita captura genérica de exceções de domínio
 * - Diferencia erros de domínio de outros tipos de erro
 * - Permite tratamento específico de erros de negócio
 *
 */
public class DomainException extends RuntimeException {

    /**
     * Construtor com mensagem.
     *
     * @param message descrição do erro
     */
    public DomainException(String message) {
        super(message);
    }

    /**
     * Construtor com mensagem e causa.
     *
     * @param message descrição do erro
     * @param cause   cause raiz da exceção
     */
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
