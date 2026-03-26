package src.Week1;

import java.util.*;
import java.util.concurrent.*;

public class Problem3 {
    // Inner class to represent DNS entries
    static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime; // in milliseconds

        DNSEntry(String domain, String ipAddress, int ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int MAX_CACHE_SIZE;
    private final Map<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    public Problem3(int maxSize) {
        this.MAX_CACHE_SIZE = maxSize;
        // LinkedHashMap with access-order = true → LRU eviction
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };

        // Background thread to clean expired entries
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            synchronized (cache) {
                Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
                while (it.hasNext()) {
                    if (it.next().getValue().isExpired()) {
                        it.remove();
                    }
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    // Simulate upstream DNS query
    private String queryUpstream(String domain) {
        // For demo: generate fake IP
        return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
    }

    // Resolve domain with TTL
    public synchronized String resolve(String domain, int ttlSeconds) {
        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ipAddress;
        } else {
            misses++;
            String ip = queryUpstream(domain);
            cache.put(domain, new DNSEntry(domain, ip, ttlSeconds));
            if (entry != null && entry.isExpired()) {
                return "Cache EXPIRED → Query upstream → " + ip;
            } else {
                return "Cache MISS → Query upstream → " + ip;
            }
        }
    }

    // Report cache statistics
    public String getCacheStats() {
        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);
        return String.format("Hit Rate: %.2f%%, Hits: %d, Misses: %d", hitRate, hits, misses);
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {
        Problem3 dnsCache = new Problem3(5);

        // First resolve → MISS
        System.out.println(dnsCache.resolve("google.com", 3));

        // Second resolve → HIT
        System.out.println(dnsCache.resolve("google.com", 3));

        // Wait for TTL expiry
        Thread.sleep(4000);
        System.out.println(dnsCache.resolve("google.com", 3));

        // Stats
        System.out.println(dnsCache.getCacheStats());
    }
}
