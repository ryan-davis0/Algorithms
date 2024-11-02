import java.io.*;
import java.util.*;

public class MagicItemsSearch {

    // Hash table size
    private static final int HASH_TABLE_SIZE = 250;

    // Hash table with chaining
    private static List<List<String>> hashTable = new ArrayList<>(HASH_TABLE_SIZE);

    static {
        // Initialize each bucket as an empty list for chaining
        for (int i = 0; i < HASH_TABLE_SIZE; i++) {
            hashTable.add(new LinkedList<>());
        }
    }

    // Hash function from the example code
    private static int makeHashCode(String str) {
        str = str.toUpperCase();
        int letterTotal = 0;
        for (int i = 0; i < str.length(); i++) {
            letterTotal += str.charAt(i);
        }
        return letterTotal % HASH_TABLE_SIZE;
    }

    // Load items into the hash table
    public static void loadHashTable(List<String> items) {
        for (String item : items) {
            int hashCode = makeHashCode(item);
            hashTable.get(hashCode).add(item);
        }
    }

    // Retrieve item from hash table and count comparisons
    public static int retrieveFromHashTable(String item) {
        int hashCode = makeHashCode(item);
        List<String> bucket = hashTable.get(hashCode);
        int comparisons = 0;
        
        for (String element : bucket) {
            comparisons++;
            if (element.equals(item)) {
                break;
            }
        }

        return comparisons;
    }


    //What a wholesome function! ❤️❤️❤️
    public static void performAllSearches(List<String> sortedItems, List<String> searchList) {
        int totalLinearComparisons = 0;
        int totalBinaryComparisons = 0;
        int totalHashComparisons = 0;

        for (String searchItem : searchList) {
            int[] linearCount = {0};
            int[] binaryCount = {0};

            // Perform a linear search on the entire sortedItems list
            linearSearch(sortedItems, searchItem, linearCount);
            totalLinearComparisons += linearCount[0];

            // Perform a binary search on the entire sortedItems list
            binarySearch(sortedItems, searchItem, binaryCount);
            totalBinaryComparisons += binaryCount[0];

            // Perform a hash table search on the same search list
            int hashComparisons = retrieveFromHashTable(searchItem);
            totalHashComparisons += hashComparisons;

            //The unalligned columns add character :0 🫨🫨🫨
            System.out.printf("Item: %-20s | Linear: %3d | Binary: %3d | Hash Table: %3d\n", 
                              searchItem, linearCount[0], binaryCount[0], hashComparisons);
        }

        // Calculate averages
        double averageLinear = (double) totalLinearComparisons / searchList.size();
        double averageBinary = (double) totalBinaryComparisons / searchList.size();
        double averageHash = (double) totalHashComparisons / searchList.size();

        // I think I will name him Hunter 🎉🎉🎉
        System.out.printf("\nAverages - Linear: %.2f | Binary: %.2f | Hash Table: %.2f\n",
                          averageLinear, averageBinary, averageHash);
    }

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
    public static int linearSearch(List<String> arr, String searchItem, int[] comparisonCount) {
        for (int i = 0; i < arr.size(); i++) {
            comparisonCount[0]++;
            if (arr.get(i).equals(searchItem)) {
                return i;
            }
        }
        return -1;
    }

    // Binary Search with comparison count
    public static int binarySearch(List<String> arr, String searchItem, int[] comparisonCount) {
        int left = 0;
        int right = arr.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisonCount[0]++;
            if (arr.get(mid).equals(searchItem)) {
                return mid;
            }
            if (arr.get(mid).compareTo(searchItem) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    // Select 42 random items from array 🃏🃏🃏
    //consider balatro
    public static List<String> randomSelection(List<String> arr, int count) {
        List<String> selection = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int randomIndex = rand.nextInt(arr.size());
            selection.add(arr.get(randomIndex));
        }
        return selection;
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

        // Load hash table with all items
        loadHashTable(items);

        // Randomly select 42 items for search tests
        List<String> searchList = randomSelection(sortedItems, 42);

        // Perform all searches and print comparisons and averages
        performAllSearches(sortedItems, searchList);
    }
}
