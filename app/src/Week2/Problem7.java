package src.Week2;
import java.util.*;

public class Problem7 {
    // Trie Node definition
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
        int frequency; // frequency of query ending here
    }

    private TrieNode root;
    private Map<String, Integer> globalFrequency; // query -> frequency

    public Problem7() {
        root = new TrieNode();
        globalFrequency = new HashMap<>();
    }

    // Insert query into Trie and update frequency
    public void insertQuery(String query) {
        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.frequency++;
        globalFrequency.put(query, globalFrequency.getOrDefault(query, 0) + 1);
    }

    // Update frequency when a query is searched again
    public void updateFrequency(String query) {
        insertQuery(query);
        System.out.println("updateFrequency(\"" + query + "\") → Frequency: " + globalFrequency.get(query));
    }

    // Get top 10 suggestions for a prefix
    public List<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return Collections.emptyList();
        }

        // Collect all queries under this prefix
        List<String> results = new ArrayList<>();
        collectQueries(node, new StringBuilder(prefix), results);

        // Sort by frequency (descending)
        results.sort((a, b) -> globalFrequency.get(b) - globalFrequency.get(a));

        // Return top 10
        return results.subList(0, Math.min(10, results.size()));
    }

    // Helper: DFS to collect queries
    private void collectQueries(TrieNode node, StringBuilder prefix, List<String> results) {
        if (node.isEndOfWord) {
            results.add(prefix.toString());
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            collectQueries(entry.getValue(), prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    // Demo
    public static void main(String[] args) {
        Problem7 autocomplete = new Problem7();

        // Insert some queries
        autocomplete.insertQuery("java tutorial");
        autocomplete.insertQuery("javascript");
        autocomplete.insertQuery("java download");
        autocomplete.insertQuery("java 21 features");
        autocomplete.insertQuery("java spring boot");
        autocomplete.insertQuery("java interview questions");

        // Simulate searches (update frequency)
        autocomplete.updateFrequency("java 21 features");
        autocomplete.updateFrequency("java 21 features");
        autocomplete.updateFrequency("java 21 features");

        // Autocomplete for prefix "jav"
        System.out.println("search(\"jav\") →");
        List<String> suggestions = autocomplete.search("jav");
        int rank = 1;
        for (String s : suggestions) {
            System.out.println(rank + ". \"" + s + "\" (" + autocomplete.globalFrequency.get(s) + " searches)");
            rank++;
        }
    }
}
