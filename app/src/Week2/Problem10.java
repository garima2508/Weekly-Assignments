package src.Week2;
import java.util.*;

public class Problem10 {
    // Video data class
    static class VideoData {
        String videoId;
        String content; // simplified representation
        VideoData(String videoId, String content) {
            this.videoId = videoId;
            this.content = content;
        }
    }

    // LRU Cache using LinkedHashMap
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;
        public LRUCache(int capacity) {
            super(capacity, 0.75f, true); // access-order
            this.capacity = capacity;
        }
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    private LRUCache<String, VideoData> L1; // memory cache
    private LRUCache<String, VideoData> L2; // SSD cache
    private Map<String, VideoData> L3;      // database

    // Stats
    private int L1Hits = 0, L1Misses = 0;
    private int L2Hits = 0, L2Misses = 0;
    private int L3Hits = 0;

    public Problem10() {
        L1 = new LRUCache<>(10000);
        L2 = new LRUCache<>(100000);
        L3 = new HashMap<>(); // simulate DB
    }

    // Add video to DB
    public void addVideoToDB(String videoId, String content) {
        L3.put(videoId, new VideoData(videoId, content));
    }

    // Get video from cache hierarchy
    public VideoData getVideo(String videoId) {
        long start = System.nanoTime();

        // L1 check
        if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("→ L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        } else {
            L1Misses++;
            System.out.println("→ L1 Cache MISS (0.5ms)");
        }

        // L2 check
        if (L2.containsKey(videoId)) {
            L2Hits++;
            System.out.println("→ L2 Cache HIT (5ms)");
            VideoData data = L2.get(videoId);
            // Promote to L1
            L1.put(videoId, data);
            System.out.println("→ Promoted to L1");
            return data;
        } else {
            L2Misses++;
            System.out.println("→ L2 Cache MISS (5ms)");
        }

        // L3 check (database)
        if (L3.containsKey(videoId)) {
            L3Hits++;
            System.out.println("→ L3 Database HIT (150ms)");
            VideoData data = L3.get(videoId);
            // Add to L2
            L2.put(videoId, data);
            System.out.println("→ Added to L2 (access count: 1)");
            return data;
        }

        System.out.println("→ Video not found!");
        return null;
    }

    // Cache statistics
    public void getStatistics() {
        int totalRequests = L1Hits + L1Misses;
        double L1HitRate = totalRequests == 0 ? 0 : (L1Hits * 100.0 / totalRequests);

        totalRequests = L2Hits + L2Misses;
        double L2HitRate = totalRequests == 0 ? 0 : (L2Hits * 100.0 / totalRequests);

        double L3HitRate = L3Hits == 0 ? 0 : (L3Hits * 100.0 / (L1Hits + L1Misses + L2Hits + L2Misses));

        System.out.println("getStatistics() →");
        System.out.printf("L1: Hit Rate %.1f%%, Avg Time: 0.5ms%n", L1HitRate);
        System.out.printf("L2: Hit Rate %.1f%%, Avg Time: 5ms%n", L2HitRate);
        System.out.printf("L3: Hit Rate %.1f%%, Avg Time: 150ms%n", L3HitRate);
    }

    // Demo
    public static void main(String[] args) {
        Problem10 cacheSystem = new Problem10();

        // Add videos to DB
        cacheSystem.addVideoToDB("video_123", "Breaking news content");
        cacheSystem.addVideoToDB("video_999", "Movie trailer content");

        // First request
        System.out.println("getVideo(\"video_123\")");
        cacheSystem.getVideo("video_123");

        // Second request (should hit L1)
        System.out.println("\ngetVideo(\"video_123\") [second request]");
        cacheSystem.getVideo("video_123");

        // Request another video
        System.out.println("\ngetVideo(\"video_999\")");
        cacheSystem.getVideo("video_999");

        // Stats
        System.out.println();
        cacheSystem.getStatistics();
    }
}
