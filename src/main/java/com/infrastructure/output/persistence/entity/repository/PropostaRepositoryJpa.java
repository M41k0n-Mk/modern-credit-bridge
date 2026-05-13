package com.infrastructure.output.persistence.entity.repository;

import com.infrastructure.output.persistence.entity.PropostaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interface Spring Data JPA para acesso baixo nível ao banco de dados.
 * Fornece operações CRUD automáticas e extensão para queries customizadas.
 * 
 * Spring Data automaticamente implementa:
 * - save(entity)
 * - findById(id)
 * - findAll()
 * - delete(entity)
 * - E muitas outras operações
 * 
 * Esta interface é interna (não deve ser usada fora de infrastruct)
 * A interface PropostaRepository (domínio) é o contrato público.
 * 
 * @author ModernBank
 */
@Repository
public interface PropostaRepositoryJpa extends JpaRepository<PropostaEntity, UUID> {
    // Spring Data fornece implementação automática
}
