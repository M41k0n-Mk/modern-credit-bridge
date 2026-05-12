package com.modernbank.credit.domain.exception;

/**
 * Exceção lançada quando uma proposta não atende aos critérios de validação de domínio.
 * 
 * Casos de uso:
 * - CPF inválido ou vazio
 * - Valor de proposta inválido ou <= 0
 * - Regras de negócio violadas
 * 
 * @author ModernBank
 */
public class PropostaInvalidaException extends DomainException {

    /**
     * Construtor com mensagem de erro.
     *
     * @param message descrição do erro de validação
     */
    public PropostaInvalidaException(String message) {
        super(message);
    }

    /**
     * Construtor com mensagem e causa raiz.
     *
     * @param message descrição do erro de validação
     * @param cause   exceção que causou o erro
     */
    public PropostaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
