package com.infrastructure.output.persistence.entity;

import com.modernbank.credit.domain.model.Proposta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidade JPA que persiste propostas no banco de dados.
 * Mapeia a tabela "propostas" com as colunas correspondentes.
 * 
 * Padrão: Anti-Corruption Layer
 * - Separa a representação de persistência da entidade de domínio
 * - Permite mudanças no banco sem afetar a lógica de negócio
 * - Facilita testes e manutenção
 * 
 * Anotações Lombok:
 * - @AllArgsConstructor: Construtor com todos os campos
 * - @NoArgsConstructor: Construtor padrão (necessário para JPA)
 * - @Getter/@Setter: Getters e setters automáticos
 *
 */
@Entity
@Table(name = "propostas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropostaEntity {

    @Id
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name = "valor", nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /**
     * Converte esta entidade JPA para uma entidade de domínio.
     *
     * @return entidade Proposta do domínio
     */
    public Proposta toDomain() {
        return new Proposta(this.id, this.cpf, this.valor, this.status);
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio.
     *
     * @param proposta entidade de domínio
     * @return entidade JPA
     */
    public static PropostaEntity fromDomain(Proposta proposta) {
        return new PropostaEntity(
                proposta.getId(),
                proposta.getCpf(),
                proposta.getValor(),
                proposta.getStatus()
        );
    }
}
