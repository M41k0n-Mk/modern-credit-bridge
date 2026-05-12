package com.infrastructure.output.persistence;

import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.repository.PropostaRepository;
import com.modernbank.credit.infrastructure.output.persistence.entity.PropostaEntity;
import com.modernbank.credit.infrastructure.output.persistence.repository.SpringDataPropostaRepository;
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
        // 1. Converte do Domínio para Entidade do Banco
        PropostaEntity entity = new PropostaEntity(
                proposta.getId(),
                proposta.getCpf(), // Assumindo que você adicione o getCpf() na classe Proposta
                proposta.getValorSolicitado(), // E o getValorSolicitado()
                proposta.getStatus(),
                proposta.getDataCriacao() // E o getDataCriacao()
        );

        // 2. Salva no RDS via Spring Data
        PropostaEntity savedEntity = springDataRepository.save(entity);

        // 3. (Opcional, mas comum) Poderíamos mapear a savedEntity de volta para o Domínio, 
        // mas como os dados são os mesmos, retornamos a própria proposta.
        return proposta;
    }

    @Override
    public Optional<Proposta> buscarPorId(UUID id) {
        // Implementação similar: busca a Entity e converte para Proposta (Domínio)
        return Optional.empty(); // Deixando vazio apenas para brevidade
    }
}