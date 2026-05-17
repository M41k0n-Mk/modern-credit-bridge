package com.modernbank.credit.context.propostas.infrastructure.output.persistence.entity.repository;

import com.modernbank.credit.context.propostas.infrastructure.output.persistence.entity.PropostaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPropostaRepository extends JpaRepository<PropostaEntity, UUID> {
  // O Spring Data já provê o save(), findById(), etc.
}
