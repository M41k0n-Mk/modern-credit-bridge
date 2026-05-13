package com.infrastructure.output.persistence;

import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.repository.PropostaRepository;
import com.infrastructure.output.persistence.entity.PropostaEntity;
import com.infrastructure.output.persistence.entity.repository.SpringDataPropostaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PropostaRepositoryAdapter implements PropostaRepository {

    private final SpringDataPropostaRepository springDataRepository;

    public PropostaRepositoryAdapter(SpringDataPropostaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Proposta salvar(Proposta proposta) {
        // Converte domínio -> entidade JPA
        PropostaEntity entity = PropostaEntity.fromDomain(proposta);
        // Persiste
        PropostaEntity saved = springDataRepository.save(entity);
        // Mapeia entidade persistida -> domínio
        return saved.toDomain();
    }

    @Override
    public Optional<Proposta> buscarPorId(UUID id) {
        // Implementação similar: busca a Entity e converte para Proposta (Domínio)
        return Optional.empty(); // Deixando vazio apenas para brevidade
    }
}