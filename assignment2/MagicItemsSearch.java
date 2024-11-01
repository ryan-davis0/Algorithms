import java.io.*;
import java.util.*;

public class MagicItemsSearch {

    // Function to capitalize the first letter of each string
    public static void capitalizeFirstLetter(List<String> arr) {
        for (int i = 0; i < arr.size(); i++) {
            String str = arr.get(i);
            if (!str.isEmpty()) {
                arr.set(i, Character.toUpperCase(str.charAt(0)) + str.substring(1));
            }
        }
    }

    // Knuth (Fisher-Yates) shuffle
    public static void knuthShuffle(List<String> arr) {
        Random rand = new Random();
        for (int i = arr.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Collections.swap(arr, i, j);
        }
    }

    // Selection Sort with comparison count
    public static void selectionSort(List<String> arr, int[] comparisonCount) {
        int n = arr.size();
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                comparisonCount[0]++;
                if (arr.get(j).compareTo(arr.get(min)) < 0) {
                    min = j;
                }
            }
            if (min != i) {
                Collections.swap(arr, i, min);
            }
        }
    }

    // Linear Search
    public static int linearSearch(List<String> arr, String target, int[] comparisonCount) {
        for (int i = 0; i < arr.size(); i++) {
            comparisonCount[0]++;
            if (arr.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }

    // Select 42 random items from array
    public static List<String> randomSelection(List<String> arr, int count) {
        List<String> selection = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int randomIndex = rand.nextInt(arr.size());
            selection.add(arr.get(randomIndex));
        }
        return selection;
    }

    // Perform Linear Search and calculate average comparisons
    public static void performLinearSearches(List<String> sortedArr, List<String> targets) {
        int totalLinearComparisons = 0;

        for (String target : targets) {
            //using an array to pass by reference
            int[] linearCount = {0};
            // Perform a linear search on the sorted array for the current target,
            // and store the number of comparisons in `linearCount[0]`
            linearSearch(sortedArr, target, linearCount);
            //add to total
            totalLinearComparisons += linearCount[0];
            System.out.println("Linear comparisons for " + target + ": " + linearCount[0]);
        }

        double averageLinear = (double) totalLinearComparisons / targets.size();
        System.out.printf("\nAverage Linear Search Comparisons: %.2f\n", averageLinear);
    }

    public static void main(String[] args) {
        List<String> items = new ArrayList<>();

        // Read from file
        try (BufferedReader br = new BufferedReader(new FileReader("assignment2/magicitems.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                items.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error: Could not open file magicitems.txt");
            return;
        }

        // Capitalize, shuffle, and sort
        capitalizeFirstLetter(items);
        knuthShuffle(items);
        List<String> sortedItems = new ArrayList<>(items);
        int[] comparisonCount = {0};
        selectionSort(sortedItems, comparisonCount);

        // Randomly select 42 items for search tests
        List<String> targets = randomSelection(sortedItems, 42);

        // Perform linear searches, then calculate average comparisons
        performLinearSearches(sortedItems, targets);
    }
}
