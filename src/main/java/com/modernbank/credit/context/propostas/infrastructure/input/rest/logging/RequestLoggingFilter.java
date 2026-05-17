package com.modernbank.credit.context.propostas.infrastructure.input.rest.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RequestLoggingFilter extends OncePerRequestFilter {

  private static final String REQ_ID = "requestId";
  private static final String TRACE_ID = "traceId";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    long start = System.currentTimeMillis();
    String requestId = getOrCreate(request, "X-Request-Id", REQ_ID);
    String traceId = getOrCreate(request, "X-Trace-Id", TRACE_ID);

    MDC.put(REQ_ID, requestId);
    MDC.put(TRACE_ID, traceId);
    MDC.put("method", request.getMethod());
    MDC.put("path", request.getRequestURI());

    if (log.isInfoEnabled()) {
      log.info(
          "[HTTP] {} {} start ts={}", request.getMethod(), request.getRequestURI(), Instant.now());
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      long took = System.currentTimeMillis() - start;
      if (log.isInfoEnabled()) {
        log.info(
            "[HTTP] {} {} end status={} tookMs={}",
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus(),
            took);
      }
      MDC.clear();
    }
  }

  private String getOrCreate(HttpServletRequest req, String header, String fallbackKey) {
    String value = req.getHeader(header);
    return (value == null || value.isBlank())
        ? (MDC.get(fallbackKey) != null ? MDC.get(fallbackKey) : UUID.randomUUID().toString())
        : value;
  }
}
