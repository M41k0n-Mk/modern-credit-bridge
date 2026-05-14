package com.modernbank.credit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernbank.credit.domain.sqs.PropostaNotifier;
import com.modernbank.credit.infrastructure.output.messaging.SqsPropostaAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

    @Bean
    public PropostaNotifier propostaNotifier(
            SqsClient sqsClient,
            ObjectMapper objectMapper,
            @Value("${cloud.aws.sqs.queue-url}") String queueUrl) {
        return new SqsPropostaAdapter(sqsClient, queueUrl, objectMapper);
    }
}