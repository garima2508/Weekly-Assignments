package src.Week2;
import java.util.*;

public class Problem9 {
    // Transaction class
    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long timestamp; // in ms

        Transaction(int id, int amount, String merchant, String account, String time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.timestamp = parseTime(time);
        }

        private long parseTime(String time) {
            // Simplified: convert HH:mm to ms
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return (hours * 60L + minutes) * 60 * 1000;
        }
    }

    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public List<String> findTwoSum(int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add("(" + map.get(complement).id + ", " + t.id + ")");
            }
            map.put(t.amount, t);
        }
        return result;
    }

    // Two-Sum with time window (1 hour)
    public List<String> findTwoSumWithWindow(int target) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < transactions.size(); i++) {
            for (int j = i + 1; j < transactions.size(); j++) {
                Transaction a = transactions.get(i);
                Transaction b = transactions.get(j);
                if (a.amount + b.amount == target &&
                        Math.abs(a.timestamp - b.timestamp) <= 3600000) {
                    result.add("(" + a.id + ", " + b.id + ")");
                }
            }
        }
        return result;
    }

    // K-Sum (recursive)
    public List<List<Integer>> findKSum(int k, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(transactions, k, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(List<Transaction> trans, int k, int target, int start,
                           List<Integer> current, List<List<Integer>> result) {
        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        if (k == 0 || target < 0) return;

        for (int i = start; i < trans.size(); i++) {
            current.add(trans.get(i).id);
            backtrack(trans, k - 1, target - trans.get(i).amount, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    // Duplicate detection
    public List<String> detectDuplicates() {
        Map<String, Map<Integer, Set<String>>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : transactions) {
            map.putIfAbsent(t.merchant, new HashMap<>());
            Map<Integer, Set<String>> amountMap = map.get(t.merchant);
            amountMap.putIfAbsent(t.amount, new HashSet<>());
            amountMap.get(t.amount).add(t.account);
        }

        for (String merchant : map.keySet()) {
            for (Map.Entry<Integer, Set<String>> entry : map.get(merchant).entrySet()) {
                if (entry.getValue().size() > 1) {
                    result.add("{amount:" + entry.getKey() + ", merchant:" + merchant +
                            ", accounts:" + entry.getValue() + "}");
                }
            }
        }
        return result;
    }

    // Demo
    public static void main(String[] args) {
        Problem9 fraudDetector = new Problem9();

        fraudDetector.addTransaction(new Transaction(1, 500, "Store A", "acc1", "10:00"));
        fraudDetector.addTransaction(new Transaction(2, 300, "Store B", "acc2", "10:15"));
        fraudDetector.addTransaction(new Transaction(3, 200, "Store C", "acc3", "10:30"));
        fraudDetector.addTransaction(new Transaction(4, 500, "Store A", "acc2", "11:00"));

        System.out.println("findTwoSum(target=500) → " + fraudDetector.findTwoSum(500));
        System.out.println("findTwoSumWithWindow(target=500) → " + fraudDetector.findTwoSumWithWindow(500));
        System.out.println("findKSum(k=3, target=1000) → " + fraudDetector.findKSum(3, 1000));
        System.out.println("detectDuplicates() → " + fraudDetector.detectDuplicates());
    }
}
