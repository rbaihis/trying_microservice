package tn.seif.ecommerce.context.threadlocal;

public class TraceParentContextManager {
    private static final ThreadLocal<String> traceParentThreadLocal = new ThreadLocal<>();

    public static void setTraceParent(String correlationId) {
        traceParentThreadLocal.set(correlationId);
    }

    public static String getTraceParent() {
        return traceParentThreadLocal.get();
    }

    public static void clear() {
        traceParentThreadLocal.remove();
    }
}
