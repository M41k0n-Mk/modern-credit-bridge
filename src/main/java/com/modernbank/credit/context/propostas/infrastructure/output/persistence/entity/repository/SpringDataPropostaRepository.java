package com.modernbank.credit.context.propostas.infrastructure.output.persistence.entity.repository;

import com.modernbank.credit.context.propostas.infrastructure.output.persistence.entity.PropostaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataPropostaRepository extends JpaRepository<PropostaEntity, UUID> {
    // O Spring Data já provê o save(), findById(), etc.
}