package tn.seif.ecommerce.config.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
@Order(-1) // Ensure this filter is executed early
public class CustomHeaderFilter implements GlobalFilter {
    private static final String TRACE_PARENT_HEADER = "TraceParent";
    public final String IDEMPOTENCY_KEY_HEADER= "Idempotency-Key";
    public final String CORRELATION_ID_HEADER= "X-correlation-Id";


    @Override
    @Order(Ordered.LOWEST_PRECEDENCE)
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {




        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();


       String correlationId = headers.getFirst(CORRELATION_ID_HEADER);
       if (correlationId == null){
           correlationId = UUID.randomUUID().toString();
           log.info("Generated new X-Correlation-Id: {}", correlationId);
       } else {
           log.info("Using existing X-Correlation-Id: {}", correlationId);
       }

       String idempotencyKey = headers.getFirst(IDEMPOTENCY_KEY_HEADER);
       if (idempotencyKey == null){
           log.info("X-Idempotency-Key not present, keeping it null.");
       } else {
           log.info("Using existing X-Idempotency-Key: {}", idempotencyKey);
       }

        String traceParent = headers.getFirst(TRACE_PARENT_HEADER);
        if (traceParent == null){
            log.info("TraceParent not present, keeping it null, brave will fill it if tracing enabled");
        } else {
            log.info("Using existing TraceParent: {}", traceParent);
        }


        // Create a new request with the modified headers
        ServerHttpRequest modifiedRequest = request.mutate()
                    .header(CORRELATION_ID_HEADER, correlationId)
                    .header(IDEMPOTENCY_KEY_HEADER, idempotencyKey)
                    .header(TRACE_PARENT_HEADER,traceParent)
                .build();


        return chain.filter(exchange);
    }
}
