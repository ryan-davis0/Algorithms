import java.io.*;
import java.util.*;

public class Assignment3 {

    // Method to process the file and create an adjacency matrix for each graph
    public static void processGraphs(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isNewGraph = false; // Tracks whether a new graph has started
            Map<String, Integer> vertexMap = new HashMap<>(); // Maps vertex IDs to matrix indices
            int vertexCount = 0; // Keeps track of the total number of vertices in the current graph
            int[][] adjacencyMatrix = null; // The adjacency matrix for the current graph

            System.out.println("Adjacency Matrices for Graphs");

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                line = line.trim(); 

                // Check if a new graph starts
                if (line.startsWith("new graph")) {
                    if (isNewGraph) {
                        // Print the adjacency matrix if there is a previous graph 
                        System.out.println("\nGraph:");
                        printMatrix(adjacencyMatrix, vertexMap);
                    }
                    // Initialize for the new graph
                    isNewGraph = true;
                    vertexMap.clear(); // Clear the vertex map for the new graph
                    vertexCount = 0; // Reset vertex count
                    adjacencyMatrix = null; // Reset adjacency matrix
                } else if (line.startsWith("add vertex")) {
                    // Parse and add a vertex
                    String vertexId = line.replace("add vertex", "").trim();

                    // Check if the vertex is not already added
                    if (!vertexMap.containsKey(vertexId)) {
                        vertexMap.put(vertexId, vertexCount); // Map vertex ID to the current matrix index
                        vertexCount++; // Increment the vertex count

                        // Resize the adjacency matrix to accommodate the new vertex
                        adjacencyMatrix = resizeMatrix(adjacencyMatrix, vertexCount);
                    }
                } else if (line.startsWith("add edge")) {
                    // Parse and add an edge
                    String[] parts = line.replace("add edge", "").split("-");
                    String u = parts[0].trim(); // Get vertex ID for u
                    String v = parts[1].trim(); // Get vertex ID for v

                    // Check if both vertices are valid
                    if (vertexMap.containsKey(u) && vertexMap.containsKey(v)) {
                        int uIndex = vertexMap.get(u); // Get the matrix index for vertex u
                        int vIndex = vertexMap.get(v); // Get the matrix index for vertex v

                        // Add both directions 
                        adjacencyMatrix[uIndex][vIndex] = 1;
                        adjacencyMatrix[vIndex][uIndex] = 1; 
                    } else {
                        // Print an error if the edge references undefined vertices
                        System.err.println("Invalid edge with undefined vertices: " + line);
                    }
                }
            }

            // Print the adjacency matrix of the last graph
            if (isNewGraph) {
                System.out.println("\nGraph:");
                printMatrix(adjacencyMatrix, vertexMap);
            }
        } catch (IOException e) {
            // Handle any errors that occur during file reading
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    // Method resize the adjacency matrix when a new vertex is added
    private static int[][] resizeMatrix(int[][] oldMatrix, int newSize) {
        int[][] newMatrix = new int[newSize][newSize];

        if (oldMatrix != null) {
            for (int i = 0; i < oldMatrix.length; i++) {
                System.arraycopy(oldMatrix[i], 0, newMatrix[i], 0, oldMatrix[i].length);
            }
        }
        return newMatrix;
    }

    // Method to print the adjacency matrix
    private static void printMatrix(int[][] matrix, Map<String, Integer> vertexMap) {
        if (matrix == null || vertexMap.isEmpty()) {
            System.out.println("No data for this graph.");
            return;
        }

        int size = matrix.length;

      // Convert the vertex map into an array where each index corresponds to a vertex ID.
        String[] vertexIds = new String[size];
        for (Map.Entry<String, Integer> entry : vertexMap.entrySet()) {
            vertexIds[entry.getValue()] = entry.getKey();
        }

        // Initial spacing for column headers
        System.out.print("    ");
        for (int i = 0; i < size; i++) {
            System.out.printf("%4s", vertexIds[i]); // Fixed width for column headers
        }
        System.out.println();

        // Print matrix rows with row headers
        for (int i = 0; i < size; i++) {
            // Print row header
            System.out.printf("%4s", vertexIds[i]);

            for (int j = 0; j < size; j++) {
                //1 for collison . for no
                if (matrix[i][j] == 1) {
                    System.out.print("   1");
                } else {
                    System.out.print("   .");
                }
            }
            System.out.println(); // Move to the next row
        }
    }

    public static void main(String[] args) {
        String fileName = "assignment3/graphs1.txt"; 
        processGraphs(fileName);
    }
}
