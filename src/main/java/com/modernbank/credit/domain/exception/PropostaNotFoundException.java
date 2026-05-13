package com.modernbank.credit.domain.exception;

/**
 * Exceção lançada quando uma proposta não é encontrada no repositório.
 * 
 * Casos de uso:
 * - Busca por ID que não existe
 * - Operações em propostas que foram deletadas
 * - Tentativa de atualizar proposta inexistente
 *
 */
public class PropostaNotFoundException extends DomainException {

    /**
     * Construtor com mensagem de erro.
     *
     * @param message descrição do erro
     */
    public PropostaNotFoundException(String message) {
        super(message);
    }

    /**
     * Construtor com mensagem e causa raiz.
     *
     * @param message descrição do erro
     * @param cause   exceção que causou o erro
     */
    public PropostaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
