package tn.seif.ecommerce.config.external;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tn.seif.ecommerce.context.threadlocal.CorrelationIdContextManager;
import tn.seif.ecommerce.context.threadlocal.IdempotencyKeyContextManager;
import tn.seif.ecommerce.context.threadlocal.TraceParentContextManager;

@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    public final String IDEMPOTENCY_KEY_HEADER= "X-Idempotency-Key";
    public final String CORRELATION_ID_HEADER= "X-correlation-Id";

    @Override
    public void apply(RequestTemplate template) {

        String correlationId = CorrelationIdContextManager.getCorrelationId();
        if (correlationId != null)
            template.header("X-Correlation-Id", correlationId);
        else
            log.warn("X-Correlation-Id is missing in the request attributes");

        String idempotencyKey = IdempotencyKeyContextManager.getIdempotencyKey();
        if (idempotencyKey != null)
            template.header("X-Idempotency-Key", idempotencyKey);
        else
            log.warn("X-Idempotency-Key is missing in the request attributes");

        String traceParent = TraceParentContextManager.getTraceParent();
        if (traceParent != null)
            template.header("TraceParent", traceParent);
        else
            log.warn("TraceParent is missing in the request attributes");



    }

}