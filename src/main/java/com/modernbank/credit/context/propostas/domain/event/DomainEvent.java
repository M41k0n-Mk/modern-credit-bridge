package com.modernbank.credit.context.propostas.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Contrato base para eventos de domínio.
 */
public interface DomainEvent {
    UUID aggregateId();
    String type();
    Instant occurredAt();
}
