package com.infrastructure.output.graph;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NeptuneGraphAdapter {

    private final Cluster cluster;

    public NeptuneGraphAdapter(@Value("${neptune.endpoint}") String endpoint) {
        this.cluster = Cluster.build()
                .addContactPoint(endpoint)
                .port(8182)
                .enableSsl(true)
                .create();
    }

    public boolean verificarFraudeCpf(String cpf) {
        GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection.using(cluster));

        // Lógica de Grafo: "Verifique se este CPF está conectado a algum nó marcado como FRAUDE"
        long conexoesSuspeitas = g.V().has("pessoa", "cpf", cpf)
                .out("vinculado_a")
                .has("status", "FRAUDE")
                .count().next();

        return conexoesSuspeitas > 0;
    }
}