package src.Week1;

import java.util.*;
import java.util.concurrent.*;

public class Problem5 {
    // Track page views
    private Map<String, Integer> pageViews = new ConcurrentHashMap<>();
    // Track unique visitors per page
    private Map<String, Set<String>> uniqueVisitors = new ConcurrentHashMap<>();
    // Track traffic sources
    private Map<String, Integer> trafficSources = new ConcurrentHashMap<>();

    // Process incoming page view event
    public void processEvent(String url, String userId, String source) {
        // Increment page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet()).add(userId);

        // Track traffic source
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Get top 10 pages by views
    private List<String> getTopPages() {
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
        pq.addAll(pageViews.entrySet());

        List<String> topPages = new ArrayList<>();
        int rank = 1;
        while (!pq.isEmpty() && rank <= 10) {
            Map.Entry<String, Integer> entry = pq.poll();
            int uniqueCount = uniqueVisitors.getOrDefault(entry.getKey(), Collections.emptySet()).size();
            topPages.add(rank + ". " + entry.getKey() + " - " +
                    entry.getValue() + " views (" + uniqueCount + " unique)");
            rank++;
        }
        return topPages;
    }

    // Get traffic source distribution
    private Map<String, String> getTrafficSourceStats() {
        Map<String, String> stats = new LinkedHashMap<>();
        int total = trafficSources.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            double percent = total == 0 ? 0 : (entry.getValue() * 100.0 / total);
            stats.put(entry.getKey(), String.format("%.1f%%", percent));
        }
        return stats;
    }

    // Dashboard output
    public void getDashboard() {
        System.out.println("Top Pages:");
        for (String page : getTopPages()) {
            System.out.println(page);
        }

        System.out.println("\nTraffic Sources:");
        for (Map.Entry<String, String> entry : getTrafficSourceStats().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {
        Problem5 dashboard = new Problem5();

        // Simulate events
        dashboard.processEvent("/article/breaking-news", "user_123", "google");
        dashboard.processEvent("/article/breaking-news", "user_456", "facebook");
        dashboard.processEvent("/sports/championship", "user_789", "direct");
        dashboard.processEvent("/sports/championship", "user_123", "google");
        dashboard.processEvent("/sports/championship", "user_456", "direct");

        // Update dashboard every 5 seconds
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(dashboard::getDashboard, 0, 5, TimeUnit.SECONDS);

        // Keep demo running for 15 seconds
        Thread.sleep(15000);
        executor.shutdown();
    }
}
