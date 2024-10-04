#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <cctype>
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

    while (std::getline(inputFile, line)) {
        if (isPalindrome(line)) {
            std::cout << "Palindrome: " << line << std::endl;// 🗣️🗣️🗣️ print the palindrome
            palindromeCount++;
        }
    }

    std::cout << "Palindromes: " << palindromeCount << std::endl;

    inputFile.close();
    return 0;
}
