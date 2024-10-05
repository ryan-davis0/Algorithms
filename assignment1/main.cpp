#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <cctype>
#include <vector>
#include <cstdlib>
#include <ctime>

// Function to capitalize the first letter of each string
void capitalizeFirstLetter(std::vector<std::string> &arr)
{
    for (std::string &str : arr)
    {
        if (!str.empty())
        {
            str[0] = std::toupper(str[0]);
        }
    }
}
// Knuth (Fisher-Yates) shuffle
void knuthShuffle(std::vector<std::string> &arr)
{
    std::srand(static_cast<unsigned int>(std::time(nullptr))); // seed random number generator

    for (int i = arr.size() - 1; i > 0; --i)
    {
        int j = std::rand() % (i + 1); // generate random index between 0 and i
        std::swap(arr[i], arr[j]);     // swap current element with random element
    }
}
// Define a template class Node. It is a generic container for any type T.
template <typename T>
class Node
{
public:
    T data;     // stores the value of the node, of generic type T
    Node *next; // next node

    Node(T value) : data(value), next(nullptr) {} // Constructor that initializes the data value and sets the next pointer to nullptr
};
// Define a template class Stack
template <typename T>
class Stack
{
private:
    Node<T> *top; // pointer to top node

public:
    // Constructor that initializes an empty stack by setting top to nullptr
    Stack() : top(nullptr) {}
    // push time!
    void push(T value)
    { // create a new node with the given value
        Node<T> *newNode = new Node<T>(value);
        newNode->next = top; // next pointer goes to current top
        top = newNode;       // become top o stack!
    }

    T pop()
    {
        if (isEmpty())
        {                                              // make sure not empty
            throw std::out_of_range("Stack is empty"); // err
        }
        Node<T> *temp = top;       // temp storage
        T poppedValue = top->data; // store the data of the top node to return later
        top = top->next;           // update the top pointer to point to the next node
        delete temp;
        return poppedValue;
    }

    bool isEmpty()
    {
        return top == nullptr;
    }

    ~Stack()
    { // destructor! 💣💣💣
        while (!isEmpty())
        {
            pop();
        }
    }
};

template <typename T>
class Queue
{
private:
    Node<T> *front; // 🎯 pointer->front
    Node<T> *rear;  // 🎯 pointer->back

public:
    Queue() : front(nullptr), rear(nullptr) {}

    void enqueue(T value)
    {
        Node<T> *newNode = new Node<T>(value); // new node with valie
        if (rear != nullptr)
        {
            rear->next = newNode; // link to rear
        }
        rear = newNode; // new node becomes rear
        if (front == nullptr)
        { // if empty becomes front
            front = rear;
        }
    }

    T dequeue()
    {
        if (isEmpty())
        {
            throw std::out_of_range("Queue is empty");
        }
        Node<T> *temp = front;         // temp storage
        T dequeuedValue = front->data; // store front node
        front = front->next;           // update front pointer
        if (front == nullptr)
        { // make rear null if q empty
            rear = nullptr;
        }
        delete temp;
        return dequeuedValue;
    }

    bool isEmpty()
    {
        return front == nullptr;
    }

    ~Queue()
    { // bomb 💣
        while (!isEmpty())
        {
            dequeue();
        }
    }
};

bool isPalindrome(const std::string &str)
{
    Stack<char> stack;
    Queue<char> queue;

    // Load stack and queue ignoring formatting stuff
    for (char ch : str)
    {
        if (std::isalpha(ch))
        {
            char lowerCh = std::tolower(ch);
            stack.push(lowerCh);
            queue.enqueue(lowerCh);
        }
    }

    // Compare characters from stack and queue
    while (!stack.isEmpty() && !queue.isEmpty())
    {
        if (stack.pop() != queue.dequeue())
        {
            return false;
        }
    }

    return true;
}
// Selection Sort for strings with comparison count
void selectionSort(std::vector<std::string> &arr, int &comparisonCount)
{
    int n = arr.size();

    for (int i = 0; i < n - 1; ++i)
    {
        int min = i;

        // Find the minimum element in the unsorted part
        for (int j = i + 1; j < n; ++j)
        {
            comparisonCount++; // Increment comparison counter
            if (arr[j] < arr[min])
            {
                min = j;
            }
        }

        // Swap minimum element with  first element
        if (min != i)
        {
            std::swap(arr[i], arr[min]);
        }
    }
}
// Insertion Sort for strings with comparison count
void insertionSort(std::vector<std::string> &arr, int &comparisonCount)
{
    int n = arr.size();
    for (int i = 1; i < n; ++i)
    {
        std::string key = arr[i];
        int j = i - 1;

        // Move elements of arr that are greater than key one pos ahead of current pos

        while (j >= 0 && arr[j] > key)
        {
            comparisonCount++;
            arr[j + 1] = arr[j];
            j = j - 1;
        }
        comparisonCount++;
        arr[j + 1] = key;
    }
}
// merge time yay!
// second nosebleed so far as well!
// making everything detailed cause i think i messed something up
void mergeSort(std::vector<std::string> &arr, int leftIndex, int middleIndex, int rightIndex, int &comparisonCount)
{
    // Determine the sizes of the two subarrays
    int leftSubarraySize = middleIndex - leftIndex + 1; // Num elements in the left subarray
    int rightSubarraySize = rightIndex - middleIndex;   // Num of elements in the right subarray

    // Create temporary vectors to store elements from left and right subarrays
    std::vector<std::string> leftSubarray(leftSubarraySize), rightSubarray(rightSubarraySize);

    // Copy elements from the original array to the temporary left subarray
    for (int i = 0; i < leftSubarraySize; i++)
    {
        leftSubarray[i] = arr[leftIndex + i];
    }

    // Copy elements from the original array to the temporary right subarray
    for (int j = 0; j < rightSubarraySize; j++)
    {
        rightSubarray[j] = arr[middleIndex + 1 + j];
    }

    // Initialize pointers for leftSubarray (i), rightSubarray (j), and the merged array (k)
    int leftPointer = 0, rightPointer = 0;
    int mergedPointer = leftIndex; // Initial index for merged array starts at the beginning of the subarray

    // This loop continues as long as both leftPointer and rightPointer
    // are within the bounds of their respective subarrays
    while (leftPointer < leftSubarraySize && rightPointer < rightSubarraySize)
    {
        comparisonCount++; // Count each comparison made between elements of the two subarrays

        // Compare elements from leftSubarray and rightSubarray, and merge them in sorted order
        if (leftSubarray[leftPointer] <= rightSubarray[rightPointer])
        {
            // If the current element in the leftSubarray is smaller or equal, place it in the merged array
            arr[mergedPointer] = leftSubarray[leftPointer];
            leftPointer++; // Move to the next element in the leftSubarray
        }
        else
        {
            // If the current element in the rightSubarray is smaller, place it in the merged array
            arr[mergedPointer] = rightSubarray[rightPointer];
            rightPointer++; // Move to the next element in the rightSubarray
        }
        mergedPointer++; // Move to the next position in the merged array
    }

    // If there are remaining elements in the leftSubarray copy them to the merged array
    while (leftPointer < leftSubarraySize)
    {
        arr[mergedPointer] = leftSubarray[leftPointer];
        leftPointer++;
        mergedPointer++;
    }

    // If there are remaining elements in the rightSubarray copy them to the merged array
    while (rightPointer < rightSubarraySize)
    {
        arr[mergedPointer] = rightSubarray[rightPointer];
        rightPointer++;
        mergedPointer++;
    }
}
// Helper function for recursive merge sort
void mergeSortHelper(std::vector<std::string> &arr, int left, int right, int &comparisonCount)
{
    if (left < right)
    {
        int middle = left + (right - left) / 2;

        // Recursively sort the left and right halves
        mergeSortHelper(arr, left, middle, comparisonCount);
        mergeSortHelper(arr, middle + 1, right, comparisonCount);

        // Merge the sorted halves
        mergeSort(arr, left, middle, right, comparisonCount);
    }
}

// Helper function to partition the array around a pivot element
int partitionArray(std::vector<std::string> &dataArray, int startIndex, int endIndex, int &comparisonCount)
{

    // Pivot is chosen as the last element in the current array segment
    std::string pivotElement = dataArray[endIndex];

    // smallerElementIndex is used to track the position where elements smaller than the pivot should go
    int smallerElementIndex = startIndex - 1;

    // Traverse the array from startIndex to endIndex - 1 (doesn't include the pivot itself)
    for (int currentIndex = startIndex; currentIndex <= endIndex - 1; currentIndex++)
    {

        // Increment comparison count for each comparison with the pivot
        comparisonCount++;

        // If the current element is smaller than the pivot element move it to the left side of the pivot
        if (dataArray[currentIndex] < pivotElement)
        {
            smallerElementIndex++;       
            std::swap(dataArray[smallerElementIndex], dataArray[currentIndex]); // Swap
        }
    }

    // Place the pivot element in its correct sorted position (after all smaller elements)
    std::swap(dataArray[smallerElementIndex + 1], dataArray[endIndex]);

    // Return the index of the pivot element, which is now in its correct position
    return smallerElementIndex + 1;
}

void quickSortArray(std::vector<std::string> &dataArray, int startIndex, int endIndex, int &comparisonCount)
{

    // Base con: if the segment has at least two elements startIndex < endIndex
    if (startIndex < endIndex)
    {

        // Partition the array and get the index where the pivot element is placed
        int pivotIndex = partitionArray(dataArray, startIndex, endIndex, comparisonCount);

        // Recursively sort the elements to the left of the pivot
        quickSortArray(dataArray, startIndex, pivotIndex - 1, comparisonCount);

        // Recursively sort the elements to the right of the pivot
        quickSortArray(dataArray, pivotIndex + 1, endIndex, comparisonCount);
    }
}
int main()
{
    std::ifstream inputFile("magicitems.txt");
    if (!inputFile)
    { // err
        std::cerr << "Error: Could not open file magicitems.txt" << std::endl;
        return 1;
    }

    std::string line;
    int palindromeCount = 0;              // Counter for palindrome
    std::vector<std::string> items;       // Store all items
    std::vector<std::string> palindromes; // Store only palindromes

    // Read each line from the file into the vector
    while (std::getline(inputFile, line))
    {
        items.push_back(line);
        if (isPalindrome(line))
        {
            palindromes.push_back(line); // Store only palindromes
            palindromeCount++;
        }
    }

    std::cout << "\nTotal Palindromes: " << palindromeCount << std::endl;
    for (const std::string &palindrome : palindromes)
    {
        std::cout << palindrome << std::endl;
    }

    // Shuffle the full set of data
    knuthShuffle(items);
    // Capitalize the first letter of each string it was annoying me that there were like 10 entries not capital
    capitalizeFirstLetter(items);
    std::vector<std::string> copy = items; // no sorting different arrays
    // Selection sort
    int selectionComparisonCount = 0;
    selectionSort(copy, selectionComparisonCount);
    copy = items;
    // Insertion Sort
    int insertionComparisonCount = 0;
    insertionSort(copy, insertionComparisonCount);
    copy = items;
    // Merge Sort
    int mergeComparisonCount = 0;
    mergeSortHelper(copy, 0, copy.size() - 1, mergeComparisonCount);
    copy = items;
    int quickComparisonCount = 0;

    // Call the QuickSort function with comparison tracking
    quickSortArray(copy, 0, copy.size() - 1, quickComparisonCount);

    std::cout << "\nData after Merge sort:\n";
    for (const std::string &item : copy)
    {
        std::cout << item << std::endl;
    }

    // Print comparison counts at the very end
    std::cout << "\nTotal Comparisons for Selection Sort: " << selectionComparisonCount << std::endl;
    std::cout << "Total Comparisons for Insertion Sort: " << insertionComparisonCount << std::endl;
    std::cout << "Total Comparisons for Merge Sort: " << mergeComparisonCount << std::endl;
    std::cout << "Total Comparisons for Quick Sort: " << quickComparisonCount << std::endl;
    inputFile.close();
    return 0;
}
