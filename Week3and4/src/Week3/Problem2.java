package Week3;
import java.util.*;
public class Problem2 {
    static class Client {
        String name;
        int riskScore;
        double accountBalance;

        Client(String name, int riskScore, double accountBalance) {
            this.name = name;
            this.riskScore = riskScore;
            this.accountBalance = accountBalance;
        }

        @Override
        public String toString() {
            return name + "(" + riskScore + ")";
        }
    }

    // Bubble Sort: ascending by riskScore, visualize swaps
    public static void bubbleSort(Client[] clients) {
        int n = clients.length;
        int swaps = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (clients[j].riskScore > clients[j + 1].riskScore) {
                    Client temp = clients[j];
                    clients[j] = clients[j + 1];
                    clients[j + 1] = temp;
                    swaps++;
                    System.out.println("Swap: " + clients[j] + " <-> " + clients[j + 1]);
                }
            }
        }
        System.out.println("BubbleSort (asc): " + Arrays.toString(clients));
        System.out.println("Total swaps: " + swaps);
    }

    // Insertion Sort: descending by riskScore, then accountBalance
    public static void insertionSort(Client[] clients) {
        int n = clients.length;
        for (int i = 1; i < n; i++) {
            Client key = clients[i];
            int j = i - 1;
            while (j >= 0 && compare(clients[j], key) < 0) {
                clients[j + 1] = clients[j];
                j--;
            }
            clients[j + 1] = key;
        }
        System.out.println("InsertionSort (desc): " + Arrays.toString(clients));
    }

    private static int compare(Client c1, Client c2) {
        if (c1.riskScore != c2.riskScore) return Integer.compare(c1.riskScore, c2.riskScore);
        return Double.compare(c1.accountBalance, c2.accountBalance);
    }

    // Identify top 10 highest risk clients
    public static void topRisks(Client[] clients, int topN) {
        System.out.print("Top " + topN + " risks: ");
        for (int i = 0; i < Math.min(topN, clients.length); i++) {
            System.out.print(clients[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Client[] sample = {
                new Client("clientC", 80, 5000.0),
                new Client("clientA", 20, 2000.0),
                new Client("clientB", 50, 3000.0)
        };

        // Bubble Sort (ascending riskScore)
        bubbleSort(Arrays.copyOf(sample, sample.length));

        // Insertion Sort (descending riskScore + accountBalance)
        Client[] sortedDesc = Arrays.copyOf(sample, sample.length);
        insertionSort(sortedDesc);

        // Top 3 risks
        topRisks(sortedDesc, 3);
    }
}
