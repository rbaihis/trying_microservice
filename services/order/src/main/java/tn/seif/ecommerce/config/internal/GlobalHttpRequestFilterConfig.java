package tn.seif.ecommerce.config.internal;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tn.seif.ecommerce.context.threadlocal.CorrelationIdContextManager;
import tn.seif.ecommerce.context.threadlocal.IdempotencyKeyContextManager;
import tn.seif.ecommerce.context.threadlocal.MdcLogContextManager;
import tn.seif.ecommerce.context.threadlocal.TraceParentContextManager;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class GlobalHttpRequestFilterConfig extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String IDEMPOTENCY_KEY_HEADER = "X-Idempotency-Key";
    public static final String TRACE_PARENT_HEADER= "TraceParent";


    @Override
    @Order(Ordered.LOWEST_PRECEDENCE)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

         request.getHeaderNames().asIterator().forEachRemaining(e ->log.info("headers-names : {}", e));

        log.info("---------__________----------------");
        log.info("my X-B3-Trace-Id:{}",request.getHeader("X-B3-Trace-Id"));
        log.info("my X-b3-trace-Id:{}",request.getHeader("X-b3-trace-id"));
        log.info("my X-B3-TraceId:{}",request.getHeader("X-B3-TraceId"));
        log.info("my X-b3-TraceId:{}",request.getHeader("X-b3-TraceId"));
        log.info("my X-b3-traceId:{}",request.getHeader("X-b3-traceId"));
        log.info("my X-b3-traceid:{}",request.getHeader("X-b3-traceid"));
        log.info("my TraceId:{}",request.getHeader("TraceId"));
        log.info("my traceId:{}",request.getHeader("traceId"));
        log.info("my traceid:{}",request.getHeader("traceid"));
        log.info("my TraceParent:{}",request.getHeader("TraceParent"));
        log.info("my Traceparent:{}",request.getHeader("traceparent"));
        log.info("---------__________----------------");

        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        String idempotencyKey = request.getHeader(IDEMPOTENCY_KEY_HEADER);
        String traceParent = request.getHeader(TRACE_PARENT_HEADER );

        CorrelationIdContextManager.setCorrelationId(correlationId);
        IdempotencyKeyContextManager.setIdempotencyKeyThreadLocal(idempotencyKey);
        TraceParentContextManager.setTraceParent(traceParent);

        MdcLogContextManager.setMdcCorrelationId(CorrelationIdContextManager.getCorrelationId());

        try {
            // to go the next chain of filter // required -mandatory
            filterChain.doFilter(request, response);
        } finally {
            // Remove correlationId from MDC to prevent it from affecting other requests this will be executed when thread is done
            MdcLogContextManager.clearMdcCorrelationId();
            CorrelationIdContextManager.clear();
            IdempotencyKeyContextManager.clear();
            TraceParentContextManager.clear();
        }
    }
}