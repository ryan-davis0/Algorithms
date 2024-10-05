#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <cctype>
#include <vector>
#include <cstdlib>
#include <ctime>

// Function to capitalize the first letter of each string
void capitalizeFirstLetter(std::vector<std::string>& arr) {
    for (std::string& str : arr) {
        if (!str.empty()) {
            str[0] = std::toupper(str[0]);
        }
    }
}
// Knuth (Fisher-Yates) shuffle
void knuthShuffle(std::vector<std::string>& arr) {
    std::srand(static_cast<unsigned int>(std::time(nullptr))); // seed random number generator

    for (int i = arr.size() - 1; i > 0; --i) {
        int j = std::rand() % (i + 1); // generate random index between 0 and i
        std::swap(arr[i], arr[j]); // swap current element with random element
    }
}
// Define a template class Node. It is a generic container for any type T.
template <typename T> 
class Node {
public:
    T data; // stores the value of the node, of generic type T
    Node* next;//next node
    
    Node(T value) : data(value), next(nullptr) {}   // Constructor that initializes the data value and sets the next pointer to nullptr
};
// Define a template class Stack
template <typename T>
class Stack {
private:
    Node<T>* top;//pointer to top node 

public:
  // Constructor that initializes an empty stack by setting top to nullptr
    Stack() : top(nullptr) {}
//push time!
    void push(T value) { // create a new node with the given value
        Node<T>* newNode = new Node<T>(value);
        newNode->next = top;// next pointer goes to current top
        top = newNode;//become top o stack!
    }

    T pop() {
        if (isEmpty()) {//make sure not empty
            throw std::out_of_range("Stack is empty");//err
        }
        Node<T>* temp = top;//temp storage
        T poppedValue = top->data;// store the data of the top node to return later
        top = top->next;// update the top pointer to point to the next node
        delete temp;
        return poppedValue;
    }

    bool isEmpty() {
        return top == nullptr;
    }

    ~Stack() {//destructor! 💣💣💣
        while (!isEmpty()) {
            pop();
        }
    }
};

template <typename T>
class Queue {
private:
    Node<T>* front;// 🎯 pointer->front
    Node<T>* rear;//🎯 pointer->back

public:
    Queue() : front(nullptr), rear(nullptr) {}

    void enqueue(T value) {
        Node<T>* newNode = new Node<T>(value);//new node with valie
        if (rear != nullptr) {
            rear->next = newNode;//link to rear
        }
        rear = newNode;//new node becomes rear
        if (front == nullptr) {// if empty becomes front
            front = rear;
        }
    }

    T dequeue() {
        if (isEmpty()) {
            throw std::out_of_range("Queue is empty");
        }
        Node<T>* temp = front;//temp storage
        T dequeuedValue = front->data;//store front node 
        front = front->next;//update front pointer
        if (front == nullptr) {//make rear null if q empty
            rear = nullptr;
        }
        delete temp;
        return dequeuedValue;
    }

    bool isEmpty() {
        return front == nullptr;
    }

    ~Queue() {//bomb 💣 
        while (!isEmpty()) {
            dequeue();
        }
    }
};

// Selection Sort for strings with comparison count
void selectionSort(std::vector<std::string>& arr, int& comparisonCount) {
    int n = arr.size();

    for (int i = 0; i < n - 1; ++i) {
        int min = i;

        // Find the minimum element in the unsorted part
        for (int j = i + 1; j < n; ++j) {
            comparisonCount++;  // Increment comparison counter
            if (arr[j] < arr[min]) {
                min = j;
            }
        }

        // Swap minimum element with  first element
        if (min != i) {
            std::swap(arr[i], arr[min]);
        }
    }
}
// Insertion Sort for strings with comparison count
void insertionSort(std::vector<std::string>& arr, int& comparisonCount) {
    int n = arr.size();
    for (int i = 1; i < n; ++i) {
        std::string key = arr[i];
        int j = i - 1;

        // Move elements of arr that are greater than key one pos ahead of current pos
 
        while (j >= 0 && arr[j] > key) {
            comparisonCount++; 
            arr[j + 1] = arr[j];
            j = j - 1;
        }
        comparisonCount++; 
        arr[j + 1] = key;
    }
}


bool isPalindrome(const std::string& str) {
    Stack<char> stack;
    Queue<char> queue;

    // Load stack and queue ignoring formatting stuff
    for (char ch : str) {
        if (std::isalpha(ch)) {
            char lowerCh = std::tolower(ch);
            stack.push(lowerCh);
            queue.enqueue(lowerCh);
        }
    }

    // Compare characters from stack and queue
    while (!stack.isEmpty() && !queue.isEmpty()) {
        if (stack.pop() != queue.dequeue()) {
            return false;
        }
    }

    return true;
}

int main() {
    std::ifstream inputFile("magicitems.txt");
    if (!inputFile) { //err
        std::cerr << "Error: Could not open file magicitems.txt" << std::endl;
        return 1;
    }

    std::string line;
    int palindromeCount = 0;  // Counter for palindrome
    std::vector<std::string> items;  // Store all items
    std::vector<std::string> palindromes;  // Store only palindromes

    // Read each line from the file into the vector
    while (std::getline(inputFile, line)) {
        items.push_back(line);  
        if (isPalindrome(line)) {
            palindromes.push_back(line);  // Store only palindromes
            palindromeCount++;
        }
    }

    std::cout << "\nTotal Palindromes: " << palindromeCount << std::endl;
    for (const std::string& palindrome : palindromes) {
        std::cout << palindrome << std::endl;
    }

    // Shuffle the full set of data
    knuthShuffle(items);
    // Capitalize the first letter of each string it was annoying me that there were like 10 entries not capital
    capitalizeFirstLetter(items);
    std::vector<std::string> copy = items;//no sorting different arrays that would 
    //mess with our comparisons between sorting algorithims 
    // Selection sort
    int selectionComparisonCount = 0;
    selectionSort(copy, selectionComparisonCount);
      copy = items;
    // Insertion Sort
    int insertionComparisonCount = 0;
    insertionSort(copy, insertionComparisonCount);

    std::cout << "\nData after Insertion Sort:\n";
    for (const std::string& item : copy) {
        std::cout << item << std::endl;
    }

    // Print comparison counts at the very end
    std::cout << "\nTotal Comparisons for Selection Sort: " << selectionComparisonCount << std::endl;
    std::cout << "Total Comparisons for Insertion Sort: " << insertionComparisonCount << std::endl;

    inputFile.close();
    return 0;
}
