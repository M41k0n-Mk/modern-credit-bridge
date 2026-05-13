package com.infrastructure.output.graph;

import jakarta.annotation.PostConstruct;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

@Component
@Profile("dev")
@ConditionalOnProperty(name = "neptune.enabled", havingValue = "true", matchIfMissing = false)
public class NeptuneDataSeeder {

    private final Cluster cluster;

    public NeptuneDataSeeder(@Value("${neptune.endpoint}") String endpoint) {
        this.cluster = Cluster.build()
                .addContactPoint(endpoint)
                .port(8182)
                .enableSsl(true)
                .create();
    }

    @PostConstruct // Executa assim que o Spring subir a aplicação
    public void popularCenariosDeTeste() {
        GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection.using(cluster));

        // Limpa o grafo (Atenção: Apenas para ambiente de DEV)
        g.V().drop().iterate();

        // Cenário 1: Cliente Bom
        g.addV("pessoa").property("cpf", "11122233344").property("nome", "Joao Limpo").property("status", "OK").iterate();

        // Cenário 2: O Fraudador e a vítima vinculada
        Vertex fraudador = g.addV("pessoa").property("cpf", "00000000000").property("status", "FRAUDE").next();
        Vertex novoCliente = g.addV("pessoa").property("cpf", "99988877766").property("status", "OK").next();

        // Cria a ligação que fará a sua query de risco estourar
        g.addE("vinculado_a").from(novoCliente).to(fraudador).iterate();

        System.out.println("====== DADOS DE TESTE NO NEPTUNE CRIADOS ======");
    }
}