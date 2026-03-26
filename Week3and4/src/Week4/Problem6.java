package Week4;
import java.util.*;
public class Problem6 {
    // Linear Search: unsorted array for threshold match
    public static void linearSearch(int[] risks, int target) {
        int comparisons = 0;
        int index = -1;
        for (int i = 0; i < risks.length; i++) {
            comparisons++;
            if (risks[i] == target) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Linear Search: threshold=" + target + " → not found");
        } else {
            System.out.println("Linear Search: threshold=" + target + " found at index " + index);
        }
        System.out.println("Comparisons: " + comparisons + " (O(n))");
    }

    // Binary Search variant: find floor and ceiling
    public static void binaryFloorCeiling(int[] risks, int target) {
        int low = 0, high = risks.length - 1;
        int comparisons = 0;
        int floor = Integer.MIN_VALUE, ceiling = Integer.MAX_VALUE;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;
            if (risks[mid] == target) {
                floor = risks[mid];
                ceiling = risks[mid];
                break;
            } else if (risks[mid] < target) {
                floor = risks[mid];
                low = mid + 1;
            } else {
                ceiling = risks[mid];
                high = mid - 1;
            }
        }

        System.out.println("Binary Search threshold=" + target);
        System.out.println("Floor: " + (floor == Integer.MIN_VALUE ? "none" : floor));
        System.out.println("Ceiling: " + (ceiling == Integer.MAX_VALUE ? "none" : ceiling));
        System.out.println("Comparisons: " + comparisons + " (O(log n))");
    }

    public static void main(String[] args) {
        int[] risks = {10, 25, 50, 100}; // sorted risk bands

        // Linear Search (unsorted simulation)
        linearSearch(risks, 30);

        // Binary Search (sorted bands)
        binaryFloorCeiling(risks, 30);
    }
}
