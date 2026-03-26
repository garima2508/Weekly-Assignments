package src.Week2;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Problem6 {
    // Inner class representing a token bucket
    static class TokenBucket {
        private final int maxTokens;
        private final int refillRate; // tokens per hour
        private AtomicInteger tokens;
        private long lastRefillTime;

        TokenBucket(int maxTokens, int refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.tokens = new AtomicInteger(maxTokens);
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens based on elapsed time
        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillTime;
            long hours = elapsed / 3600000; // convert ms to hours

            if (hours >= 1) {
                tokens.set(maxTokens); // reset every hour
                lastRefillTime = now;
            }
        }

        // Try to consume a token
        synchronized boolean allowRequest() {
            refill();
            if (tokens.get() > 0) {
                tokens.decrementAndGet();
                return true;
            }
            return false;
        }

        int getRemainingTokens() {
            refill();
            return tokens.get();
        }

        long getResetTime() {
            return lastRefillTime + 3600000; // next reset in ms
        }
    }

    // Map of clientId -> TokenBucket
    private final Map<String, TokenBucket> clients = new ConcurrentHashMap<>();
    private final int MAX_TOKENS = 1000; // per hour
    private final int REFILL_RATE = 1000; // reset every hour

    // Check rate limit for a client
    public String checkRateLimit(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(MAX_TOKENS, REFILL_RATE));
        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        } else {
            long retryAfter = (bucket.getResetTime() - System.currentTimeMillis()) / 1000;
            return "Denied (0 requests remaining, retry after " + retryAfter + "s)";
        }
    }

    // Get status for a client
    public String getRateLimitStatus(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(MAX_TOKENS, REFILL_RATE));
        TokenBucket bucket = clients.get(clientId);

        int used = MAX_TOKENS - bucket.getRemainingTokens();
        return "{used: " + used + ", limit: " + MAX_TOKENS + ", reset: " + bucket.getResetTime() + "}";
    }

    // Demo
    public static void main(String[] args) {
        Problem6 limiter = new Problem6();

        String clientId = "abc123";

        // Simulate requests
        for (int i = 0; i < 1002; i++) {
            System.out.println("checkRateLimit(clientId=\"" + clientId + "\") → " + limiter.checkRateLimit(clientId));
        }

        // Status
        System.out.println("getRateLimitStatus(\"" + clientId + "\") → " + limiter.getRateLimitStatus(clientId));
    }
}
