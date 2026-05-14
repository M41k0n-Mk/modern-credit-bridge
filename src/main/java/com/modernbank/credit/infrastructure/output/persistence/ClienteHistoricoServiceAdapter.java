package com.modernbank.credit.infrastructure.output.persistence;

import com.modernbank.credit.domain.service.ClienteHistoricoService;
import com.modernbank.credit.domain.service.RiscoCliente;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

/**
 * Adapter de infraestrutura que simula a consulta ao Neptune
 * para avaliar o risco de um cliente. Em produção, este componente
 * faria chamadas via mensageria/HTTP/conectores proprietários.
 */
@Component
public class ClienteHistoricoServiceAdapter implements ClienteHistoricoService {

    private final Cluster cluster;

    // O Spring injeta a URL do Neptune que o Terraform criou
    public ClienteHistoricoServiceAdapter(@Value("${neptune.endpoint}") String endpoint) {
        this.cluster = Cluster.build()
                .addContactPoint(endpoint)
                .port(8182)
                .enableSsl(true)
                .create();
    }

    @Override
    public RiscoCliente avaliarRisco(String cpf, BigDecimal valor) {
        if (cpf == null || cpf.isBlank()) {
            return RiscoCliente.MEDIO;
        }

        // Abre a conexão com o grafo
        GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection.using(cluster));

        // Consulta no Grafo: O CPF fornecido está a 1 salto de distância de um fraudador conhecido?
        long conexoesSuspeitas = g.V().has("pessoa", "cpf", cpf)
                .out("vinculado_a")
                .has("status", "FRAUDE")
                .count().next();

        // Se encontrou conexão com fraude, o risco é ALTO e o UseCase vai REJEITAR
        if (conexoesSuspeitas > 0) {
            return RiscoCliente.ALTO;
        }

        // Mantendo sua heurística de negócio: Valores altos vão para análise manual
        if (valor != null && valor.compareTo(new BigDecimal("10000")) > 0) {
            return RiscoCliente.MEDIO;
        }

        // Cliente limpo
        return RiscoCliente.BAIXO;
    }
}
