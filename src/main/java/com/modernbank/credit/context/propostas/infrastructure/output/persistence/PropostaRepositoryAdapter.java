package com.modernbank.credit.context.propostas.infrastructure.output.persistence;

import com.modernbank.credit.context.propostas.domain.model.Proposta;
import com.modernbank.credit.context.propostas.domain.repository.PropostaRepository;
import com.modernbank.credit.context.propostas.infrastructure.output.persistence.entity.PropostaEntity;
import com.modernbank.credit.context.propostas.infrastructure.output.persistence.entity.repository.SpringDataPropostaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PropostaRepositoryAdapter implements PropostaRepository {

  private final SpringDataPropostaRepository springDataRepository;

  public PropostaRepositoryAdapter(SpringDataPropostaRepository springDataRepository) {
    this.springDataRepository = springDataRepository;
  }

  @Override
  public Proposta salvar(Proposta proposta) {
    if (log.isDebugEnabled()) {
      log.debug(
          "[PropostaRepositoryAdapter] Salvando proposta. cpf=***{} valor={} status={}",
          mask(proposta.getCpf().getValor()),
          proposta.getValor().getValor(),
          proposta.getStatus().name());
    }
    // Converte domínio -> entidade JPA
    PropostaEntity entity = PropostaEntity.fromDomain(proposta);
    // Persiste
    PropostaEntity saved = springDataRepository.save(entity);
    // Mapeia entidade persistida -> domínio
    Proposta dominio = saved.toDomain();
    if (log.isInfoEnabled()) {
      log.info(
          "[PropostaRepositoryAdapter] Proposta salva. id={} status={}",
          dominio.getId(),
          dominio.getStatus());
    }
    return dominio;
  }

  @Override
  public Optional<Proposta> buscarPorId(UUID id) {
    if (log.isDebugEnabled()) {
      log.debug("[PropostaRepositoryAdapter] Buscando proposta por id={}", id);
    }
    return springDataRepository.findById(id).map(PropostaEntity::toDomain);
  }

  private String mask(String cpf) {
    if (cpf == null || cpf.length() < 3) return "***";
    return "***" + cpf.substring(cpf.length() - 3);
  }
}
