package com.modernbank.credit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernbank.credit.context.propostas.domain.sqs.PropostaNotifier;
import com.modernbank.credit.context.propostas.infrastructure.output.messaging.SqsPropostaAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

  @Value("${cloud.aws.region.static:us-east-1}")
  private String region;

  @Bean
  public SqsClient sqsClient() {
    return SqsClient.builder().region(Region.of(region)).build();
  }

  @Bean
  public PropostaNotifier propostaNotifier(
      SqsClient sqsClient,
      ObjectMapper objectMapper,
      @Value("${cloud.aws.sqs.queue-url}") String queueUrl) {
    return new SqsPropostaAdapter(sqsClient, queueUrl, objectMapper);
  }
}
