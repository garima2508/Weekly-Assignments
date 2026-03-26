package src.Week1;

import java.util.*;
public class Problem1 {
        private Map<String, Integer> usernames;
        private Map<String, Integer> attemptFrequency;
        public Problem1() {
            usernames = new HashMap<>();
            attemptFrequency = new HashMap<>();
        }
        public void registerUser(String username, int userId) {
            usernames.put(username, userId);
        }

        public boolean checkAvailability(String username) {
            attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);
            return !usernames.containsKey(username);
        }
        public List<String> suggestAlternatives(String username) {
            List<String> suggestions = new ArrayList<>();
            String[] candidates = {
                    username + "1",
                    username + "2",
                    username.replace("_", "."),
                    "the_" + username,
                    username + "_official"
            };

            for (String candidate : candidates) {
                if (!usernames.containsKey(candidate)) {
                    suggestions.add(candidate);
                }
            }
            return suggestions;
        }
        public String getMostAttempted() {
            return attemptFrequency.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }
        public static void main(String[] args) {
            Problem1 system = new Problem1();
            system.registerUser("john_doe", 1);
            system.registerUser("admin", 2);
            System.out.println("checkAvailability(\"john_doe\") → " + system.checkAvailability("john_doe"));
            System.out.println("checkAvailability(\"jane_smith\") → " + system.checkAvailability("jane_smith"));

            System.out.println("suggestAlternatives(\"john_doe\") → " + system.suggestAlternatives("john_doe"));

            for (int i = 0; i < 10543; i++) {
                system.checkAvailability("admin");
            }
            System.out.println("getMostAttempted() → " + system.getMostAttempted());
        }
}