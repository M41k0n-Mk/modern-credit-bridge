package com.modernbank.credit.infrastructure.output.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernbank.credit.domain.event.DomainEvent;
import com.modernbank.credit.domain.event.PropostaCriadaEvent;
import com.modernbank.credit.domain.sqs.PropostaNotifier;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
public class SqsPropostaAdapter implements PropostaNotifier {

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;

    public SqsPropostaAdapter(SqsClient sqsClient, String queueUrl, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicar(DomainEvent event) {
        try {
            String json;
            // Backward compatibility: para PropostaCriadaEvent manter o contrato antigo da fila (payload de Proposta)
            if (event instanceof PropostaCriadaEvent e) {
                var payloadCompat = new java.util.LinkedHashMap<String, Object>();
                payloadCompat.put("id", e.getPropostaId());
                payloadCompat.put("cpf", e.getCpf());
                payloadCompat.put("valor", e.getValor());
                payloadCompat.put("status", e.getStatus().name());
                json = objectMapper.writeValueAsString(payloadCompat);
            } else {
                // Para outros eventos, publica o próprio evento (envelope com type/occurredAt/aggregateId)
                json = objectMapper.writeValueAsString(event);
            }
            if (log.isDebugEnabled()) {
                log.debug("[SqsPropostaAdapter] Enviando evento para SQS. queueUrl={} type={} aggregateId={}", queueUrl, event.type(), event.aggregateId());
            }
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .build());
            if (log.isInfoEnabled()) {
                log.info("[SqsPropostaAdapter] Evento publicado na SQS com sucesso. type={} aggregateId={}", event.type(), event.aggregateId());
            }
        } catch (Exception e) {
            log.error("[SqsPropostaAdapter] Erro ao publicar evento na SQS. cause={}", e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar para o SQS", e);
        }
    }
}