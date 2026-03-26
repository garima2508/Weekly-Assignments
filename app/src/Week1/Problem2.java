package src.Week1;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
public class Problem2 {
    // productId -> stock count
    private Map<String, AtomicInteger> inventory;
    // productId -> waiting list (FIFO)
    private Map<String, Queue<Integer>> waitingList;

    public Problem2() {
        inventory = new ConcurrentHashMap<>();
        waitingList = new ConcurrentHashMap<>();
    }

    // Initialize product with stock
    public void addProduct(String productId, int stockCount) {
        inventory.put(productId, new AtomicInteger(stockCount));
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock availability
    public String checkStock(String productId) {
        AtomicInteger stock = inventory.get(productId);
        if (stock == null) return "Product not found";
        return stock.get() + " units available";
    }

    // Purchase item safely
    public synchronized String purchaseItem(String productId, int userId) {
        AtomicInteger stock = inventory.get(productId);
        if (stock == null) return "Product not found";

        if (stock.get() > 0) {
            int remaining = stock.decrementAndGet();
            return "Success, " + remaining + " units remaining";
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    // Show waiting list for a product
    public List<Integer> getWaitingList(String productId) {
        return new ArrayList<>(waitingList.getOrDefault(productId, new LinkedList<>()));
    }

    // Demo
    public static void main(String[] args) {
        Problem2 system = new Problem2();

        // Add product with 100 units
        system.addProduct("IPHONE15_256GB", 100);

        // Check stock
        System.out.println("checkStock(\"IPHONE15_256GB\") → " + system.checkStock("IPHONE15_256GB"));

        // Simulate purchases
        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345)); // Success
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890)); // Success

        // Exhaust stock
        for (int i = 0; i < 98; i++) {
            system.purchaseItem("IPHONE15_256GB", i);
        }

        // Now stock is 0
        System.out.println(system.purchaseItem("IPHONE15_256GB", 99999)); // Added to waiting list
        System.out.println(system.purchaseItem("IPHONE15_256GB", 100000)); // Added to waiting list

        // Show waiting list
        System.out.println("Waiting list: " + system.getWaitingList("IPHONE15_256GB"));
    }
}
