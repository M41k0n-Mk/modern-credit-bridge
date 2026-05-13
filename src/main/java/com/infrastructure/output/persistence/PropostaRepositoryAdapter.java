package com.infrastructure.output.persistence;

import com.infrastructure.output.persistence.entity.PropostaEntity;
import com.infrastructure.output.persistence.entity.repository.SpringDataPropostaRepository;
import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.repository.PropostaRepository;
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
        // Converte do Domínio para Entidade do Banco (factory da própria entidade)
        PropostaEntity entity = PropostaEntity.fromDomain(proposta);

        // Persiste via Spring Data
        PropostaEntity saved = springDataRepository.save(entity);

        // Converte de volta para Domínio e retorna
        return saved.toDomain();
    }

    @Override
    public Optional<Proposta> buscarPorId(UUID id) {
        return springDataRepository.findById(id).map(PropostaEntity::toDomain);
    }
}