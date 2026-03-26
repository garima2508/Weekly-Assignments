package Week3;
import java.util.*;
public class Problem1 {
    static class Transaction {
        String id;
        double fee;
        String timestamp; // simplified as string for demo

        Transaction(String id, double fee, String timestamp) {
            this.id = id;
            this.fee = fee;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return id + ": " + fee + "@" + timestamp;
        }
    }

    // Bubble Sort: ascending by fee
    public static void bubbleSort(List<Transaction> transactions) {
        int n = transactions.size();
        int swaps = 0, passes = 0;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            passes++;
            for (int j = 0; j < n - i - 1; j++) {
                if (transactions.get(j).fee > transactions.get(j + 1).fee) {
                    Collections.swap(transactions, j, j + 1);
                    swaps++;
                    swapped = true;
                }
            }
            if (!swapped) break; // early termination
        }
        System.out.println("BubbleSort result: " + transactions);
        System.out.println("Passes: " + passes + ", Swaps: " + swaps);
    }

    // Insertion Sort: ascending by fee, then timestamp
    public static void insertionSort(List<Transaction> transactions) {
        int n = transactions.size();
        for (int i = 1; i < n; i++) {
            Transaction key = transactions.get(i);
            int j = i - 1;
            while (j >= 0 && compare(transactions.get(j), key) > 0) {
                transactions.set(j + 1, transactions.get(j));
                j--;
            }
            transactions.set(j + 1, key);
        }
        System.out.println("InsertionSort result: " + transactions);
    }

    private static int compare(Transaction t1, Transaction t2) {
        if (t1.fee != t2.fee) return Double.compare(t1.fee, t2.fee);
        return t1.timestamp.compareTo(t2.timestamp); // stable tie-breaker
    }

    // Outlier detection
    public static void flagOutliers(List<Transaction> transactions) {
        List<Transaction> outliers = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.fee > 50.0) outliers.add(t);
        }
        if (outliers.isEmpty()) {
            System.out.println("High-fee outliers: none");
        } else {
            System.out.println("High-fee outliers: " + outliers);
        }
    }

    public static void main(String[] args) {
        List<Transaction> sample = new ArrayList<>();
        sample.add(new Transaction("id1", 10.5, "10:00"));
        sample.add(new Transaction("id2", 25.0, "09:30"));
        sample.add(new Transaction("id3", 5.0, "10:15"));

        // Small batch → Bubble Sort
        bubbleSort(new ArrayList<>(sample));

        // Medium batch → Insertion Sort
        insertionSort(new ArrayList<>(sample));

        // Outlier detection
        flagOutliers(sample);
    }
}
