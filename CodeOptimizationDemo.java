package task;

import java.util.HashMap;
import java.util.Map;

public class CodeOptimizationDemo {

    public static void main(String[] args) {
        // Input array for testing
        int[] numbers = {1, 2, 3, 2, 4, 3, 5, 1};

        // Using old nested loop implementation (before optimization)
        System.out.println("Duplicate count (Before Optimization): " + calculateUsingNestedLoops(numbers));

        // Using optimized implementation with HashMap
        System.out.println("Duplicate count (After Optimization): " + calculateDuplicateCount(numbers));
    }

    // Before Optimization: Nested loops to count duplicates
    public static int calculateUsingNestedLoops(int[] numbers) {
        int duplicateCount = 0;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] == numbers[j]) {
                    duplicateCount++;
                    break; // Avoid counting the same duplicate multiple times
                }
            }
        }
        return duplicateCount;
    }

    // After Optimization: Using HashMap for efficiency
    public static int calculateDuplicateCount(int[] numbers) {
        // Frequency map to count occurrences of each number
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int number : numbers) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }

        // Counting duplicates where frequency > 1
        int duplicateCount = 0;
        for (int count : frequencyMap.values()) {
            if (count > 1) {
                duplicateCount++;
            }
        }
        return duplicateCount;
    }
}
