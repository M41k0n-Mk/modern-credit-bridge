package com.modernbank.credit.infrastructure.output.messaging;

import com.modernbank.credit.domain.model.Proposta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernbank.credit.domain.sqs.PropostaNotifier;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import lombok.extern.slf4j.Slf4j;

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
    public void notificarCriacao(Proposta proposta) {
        try {
            String json = objectMapper.writeValueAsString(proposta);
            if (log.isDebugEnabled()) {
                log.debug("[SqsPropostaAdapter] Enviando mensagem para SQS. queueUrl={} id={}", queueUrl, proposta.getId());
            }
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .build());
            if (log.isInfoEnabled()) {
                log.info("[SqsPropostaAdapter] Mensagem enviada para SQS com sucesso. id={}", proposta.getId());
            }
        } catch (Exception e) {
            log.error("[SqsPropostaAdapter] Erro ao enviar mensagem para SQS. id={} cause={}",
                    proposta != null ? proposta.getId() : null, e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar para o SQS", e);
        }
    }
}