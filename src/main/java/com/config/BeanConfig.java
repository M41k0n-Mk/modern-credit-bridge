package com.config;

import com.modernbank.credit.domain.repository.PropostaRepository;
import com.modernbank.credit.domain.usecase.CriarPropostaUseCase;
import com.modernbank.credit.domain.service.ClienteHistoricoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de Beans da aplicação.
 * Define os beans de injeção de dependência para use cases e outros componentes.
 */
@Configuration
public class BeanConfig {

    /**
     * Factory Bean para CriarPropostaUseCase.
     * Injeta as dependências necessárias (repository) via IoC Container Spring.
     *
     * @param repository repositório de propostas
     * @return instância do use case configurada
     */
    @Bean
    public CriarPropostaUseCase criarPropostaUseCase(PropostaRepository repository,
                                                    ClienteHistoricoService clienteHistoricoService) {
        return new CriarPropostaUseCase(repository, clienteHistoricoService);
    }
}