package com.modernbank.credit.infrastructure.output.graph;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GraphBinaryMessageSerializerV1;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NeptuneGraphAdapter {

    private final Cluster cluster;

    public NeptuneGraphAdapter(@Value("${neptune.endpoint}") String endpoint) {
        if (log.isInfoEnabled()) {
            log.info("[NeptuneGraphAdapter] Inicializando conexão com endpoint={} ssl=true", endpoint);
        }
        this.cluster = Cluster.build()
                .addContactPoint(endpoint)
                .port(8182)
                .enableSsl(true)
                .serializer(new GraphBinaryMessageSerializerV1())
                .create();
    }

    public boolean verificarFraudeCpf(String cpf) {
        long start = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            String masked = (cpf != null && cpf.length() >= 3) ? ("***" + cpf.substring(cpf.length()-3)) : "(indefinido)";
            log.debug("[NeptuneGraphAdapter] Verificando conexões suspeitas. cpf={}", masked);
        }
        GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection.using(cluster));

        // Lógica de Grafo: "Verifique se este CPF está conectado a algum nó marcado como FRAUDE"
        long conexoesSuspeitas = g.V().has("pessoa", "cpf", cpf)
                .out("vinculado_a")
                .has("status", "FRAUDE")
                .count().next();

        boolean suspeito = conexoesSuspeitas > 0;
        if (log.isInfoEnabled()) {
            long took = System.currentTimeMillis() - start;
            log.info("[NeptuneGraphAdapter] Verificação concluída. suspeito={} tookMs={}", suspeito, took);
        }
        return suspeito;
    }
}