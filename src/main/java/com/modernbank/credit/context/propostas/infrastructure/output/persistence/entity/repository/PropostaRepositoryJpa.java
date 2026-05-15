package com.modernbank.credit.context.propostas.infrastructure.output.persistence.entity.repository;

import com.modernbank.credit.context.propostas.infrastructure.output.persistence.PropostaRepositoryAdapter;
import com.modernbank.credit.context.propostas.infrastructure.output.persistence.entity.PropostaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * DEPRECATED: Interface antiga de acesso JPA. Mantida apenas para compatibilidade histórica.
 * Não é registrada como bean Spring para evitar duplicidades.
 * Utilize {@link SpringDataPropostaRepository}
 * junto com o adapter {@link PropostaRepositoryAdapter}.
 */
@Deprecated
public interface PropostaRepositoryJpa extends JpaRepository<PropostaEntity, UUID> {
    // Spring Data fornece implementação automática
}
