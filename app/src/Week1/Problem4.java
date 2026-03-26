package src.Week1;

import java.util.*;
public class Problem4 {
    // Map of n-gram -> set of document IDs
    private Map<String, Set<String>> ngramIndex = new HashMap<>();
    private int n = 5; // default to 5-grams

    // Add a document to the index
    public void addDocument(String docId, String text) {
        List<String> words = Arrays.asList(text.split("\\s+"));
        for (int i = 0; i <= words.size() - n; i++) {
            String ngram = String.join(" ", words.subList(i, i + n));
            ngramIndex.computeIfAbsent(ngram, k -> new HashSet<>()).add(docId);
        }
    }

    // Analyze a new document against existing ones
    public void analyzeDocument(String docId, String text) {
        List<String> words = Arrays.asList(text.split("\\s+"));
        int totalNgrams = Math.max(0, words.size() - n + 1);

        // Count matches per document
        Map<String, Integer> matchCounts = new HashMap<>();

        for (int i = 0; i <= words.size() - n; i++) {
            String ngram = String.join(" ", words.subList(i, i + n));
            if (ngramIndex.containsKey(ngram)) {
                for (String existingDoc : ngramIndex.get(ngram)) {
                    matchCounts.put(existingDoc, matchCounts.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        System.out.println("analyzeDocument(\"" + docId + "\")");
        System.out.println("→ Extracted " + totalNgrams + " n-grams");

        // Report similarity
        for (Map.Entry<String, Integer> entry : matchCounts.entrySet()) {
            double similarity = (entry.getValue() * 100.0) / totalNgrams;
            System.out.printf("→ Found %d matching n-grams with \"%s\"%n", entry.getValue(), entry.getKey());
            System.out.printf("→ Similarity: %.1f%% %s%n", similarity,
                    similarity > 50 ? "(PLAGIARISM DETECTED)" : (similarity > 10 ? "(suspicious)" : ""));
        }

        // Finally, add this document to the index
        addDocument(docId, text);
    }

    // Demo
    public static void main(String[] args) {
        Problem4 detector = new Problem4();

        // Add some existing documents
        detector.addDocument("essay_089.txt", "This is a sample essay with some unique content and repeated phrases.");
        detector.addDocument("essay_092.txt", "This essay contains a lot of similar content and repeated phrases for testing plagiarism detection.");

        // Analyze a new document
        detector.analyzeDocument("essay_123.txt", "This essay contains a lot of similar content and repeated phrases for testing plagiarism detection. It also has some unique content.");
    }
}