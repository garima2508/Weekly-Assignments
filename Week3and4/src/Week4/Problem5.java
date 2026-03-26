package Week4;
import java.util.*;
public class Problem5 {
    public static void linearSearch(String[] logs, String target) {
        int first = -1, last = -1, comparisons = 0;
        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].equals(target)) {
                if (first == -1) first = i;
                last = i;
            }
        }
        System.out.println("Linear Search for " + target + ":");
        System.out.println("First occurrence: " + first + ", Last occurrence: " + last);
        System.out.println("Comparisons: " + comparisons + " (O(n))");
    }

    // Binary Search: exact match + count duplicates
    public static void binarySearch(String[] logs, String target) {
        int comparisons = 0;
        int low = 0, high = logs.length - 1;
        int foundIndex = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;
            if (logs[mid].equals(target)) {
                foundIndex = mid;
                break;
            } else if (logs[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (foundIndex == -1) {
            System.out.println("Binary Search: " + target + " not found");
            System.out.println("Comparisons: " + comparisons + " (O(log n))");
            return;
        }

        // Count duplicates by expanding left/right
        int count = 1;
        int left = foundIndex - 1;
        while (left >= 0 && logs[left].equals(target)) {
            count++;
            left--;
        }
        int right = foundIndex + 1;
        while (right < logs.length && logs[right].equals(target)) {
            count++;
            right++;
        }

        System.out.println("Binary Search for " + target + ": index=" + foundIndex + ", count=" + count);
        System.out.println("Comparisons: " + comparisons + " (O(log n))");
    }

    public static void main(String[] args) {
        String[] logs = {"accB", "accA", "accB", "accC"};

        // Linear Search
        linearSearch(logs, "accB");

        // Sort logs for Binary Search
        Arrays.sort(logs);
        System.out.println("Sorted logs: " + Arrays.toString(logs));

        // Binary Search
        binarySearch(logs, "accB");
    }
}
