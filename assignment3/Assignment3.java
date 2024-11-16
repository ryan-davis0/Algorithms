import java.io.*;
import java.util.*;

public class Assignment3 {

    // Vertex class for the linked objects representation
    static class Vertex {
        String id; // ID of the vertex
        boolean processed; // Whether the vertex is processed
        List<Vertex> neighbors; // List of neighboring vertices

        Vertex(String id) {
            this.id = id;
            this.processed = false;
            this.neighbors = new ArrayList<>();
        }

        void addNeighbor(Vertex neighbor) {
            this.neighbors.add(neighbor);
        }
    }

    // Method to process the file and create adjacency matrix, adjacency list, and linked objects version
    public static void processGraphs(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isNewGraph = false; // Tracks whether a new graph has started
            Map<String, Integer> vertexMap = new HashMap<>();
            // Maps each vertex ID (String) to a unique integer index used in the adjacency matrix.
            Map<String, TreeSet<String>> adjacencyList = new HashMap<>(); // Stores adjacency list for the graph
            Map<String, Vertex> linkedGraph = new HashMap<>(); // Linked objects representation
            int vertexCount = 0; // Keeps track of the total number of vertices in the current graph
            int[][] adjacencyMatrix = null; // The adjacency matrix for the current graph

            System.out.println("Adjacency Representations for Graphs");

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Check if a new graph starts
                if (line.startsWith("new graph")) {
                    if (isNewGraph) {
                        // Print the adjacency matrix if there is a previous graph
                        System.out.println("\nGraph:");
                        printMatrix(adjacencyMatrix, vertexMap);
                        System.out.println("\nAdjacency List:");
                        printAdjacencyList(adjacencyList);
                        System.out.println("\nLinked Objects Traversals:");
                        performTraversals(linkedGraph);
                    }
                    // Initialize for the new graph
                    isNewGraph = true;
                    vertexMap.clear(); // Clear the vertex map for the new graph
                    adjacencyList.clear(); // Clear the adjacency list for the new graph
                    linkedGraph.clear(); // Clear the linked graph for the new graph
                    vertexCount = 0; // Reset vertex count
                    adjacencyMatrix = null; // Reset adjacency matrix
                } else if (line.startsWith("add vertex")) {
                    // Parse and add a vertex
                    String vertexId = line.replace("add vertex", "").trim();

                    // Check if the vertex is not already added
                    if (!vertexMap.containsKey(vertexId)) {
                        vertexMap.put(vertexId, vertexCount); // Map vertex ID to the current matrix index
                        // Initialize adjacency list with numerical sorting
                        adjacencyList.put(vertexId, new TreeSet<>(Comparator.comparingInt(Integer::parseInt)));
                        linkedGraph.put(vertexId, new Vertex(vertexId)); // Add vertex to linked graph
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

                        // Add both directions in matrix
                        adjacencyMatrix[uIndex][vIndex] = 1;
                        adjacencyMatrix[vIndex][uIndex] = 1;

                        // Add both directions in the adjacency list
                        adjacencyList.get(u).add(v);
                        adjacencyList.get(v).add(u);

                        // Add edge to linked graph
                        linkedGraph.get(u).addNeighbor(linkedGraph.get(v));
                        linkedGraph.get(v).addNeighbor(linkedGraph.get(u));
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
                System.out.println("\nAdjacency List:");
                printAdjacencyList(adjacencyList);
                System.out.println("\nLinked Objects Traversals:");
                performTraversals(linkedGraph);
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
                // 1 for edge, . for no edge
                if (matrix[i][j] == 1) {
                    System.out.print("   1");
                } else {
                    System.out.print("   .");
                }
            }
            System.out.println(); // Move to the next row
        }
    }

    // Method to print the adjacency list 
    private static void printAdjacencyList(Map<String, TreeSet<String>> adjacencyList) {
        if (adjacencyList.isEmpty()) {
            System.out.println("No data for this graph.");
            return;
        }

        // Sort keys (vertices) numerically
        List<String> sortedKeys = new ArrayList<>(adjacencyList.keySet());
        sortedKeys.sort(Comparator.comparingInt(Integer::parseInt));

        // Iterate through each vertex in the adjacency list
        for (String key : sortedKeys) {
            System.out.print(key + ": ");
            System.out.println(String.join(", ", adjacencyList.get(key)));
        }
    }

    // Perform depth-first and breadth-first traversals on the linked graph
    private static void performTraversals(Map<String, Vertex> linkedGraph) {
        if (linkedGraph.isEmpty()) {
            System.out.println("No data for linked objects traversal.");
            return;
        }

        // Perform depth-first traversal
        System.out.println("Depth-First Traversal:");
        Set<String> visited = new HashSet<>();
        // Sort the keys numerically to start traversal in order
        List<String> sortedKeys = new ArrayList<>(linkedGraph.keySet());
        sortedKeys.sort(Comparator.comparingInt(Integer::parseInt));
        for (String key : sortedKeys) {
            if (!visited.contains(key)) {
                depthFirstTraversal(linkedGraph.get(key), visited);
            }
        }
        System.out.println();

        // Perform breadth-first traversal
        System.out.println("Breadth-First Traversal:");
        visited.clear();
        for (String key : sortedKeys) {
            if (!visited.contains(key)) {
                breadthFirstTraversal(linkedGraph.get(key), visited);
            }
        }
        System.out.println();
    }

    // Depth-first traversal using recursion
    private static void depthFirstTraversal(Vertex vertex, Set<String> visited) {
        if (!visited.add(vertex.id)) return;
        System.out.print(vertex.id + " ");
        // Sort neighbors numerically for consistent traversal order
        vertex.neighbors.sort(Comparator.comparingInt(v -> Integer.parseInt(v.id)));
        for (Vertex neighbor : vertex.neighbors) {
            if (!visited.contains(neighbor.id)) {
                depthFirstTraversal(neighbor, visited);
            }
        }
    }

    // Breadth-first traversal using a queue
    private static void breadthFirstTraversal(Vertex startVertex, Set<String> visited) {
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(startVertex);
        visited.add(startVertex.id);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            System.out.print(current.id + " ");
            // Sort neighbors numerically for consistent traversal order
            current.neighbors.sort(Comparator.comparingInt(v -> Integer.parseInt(v.id)));
            for (Vertex neighbor : current.neighbors) {
                if (visited.add(neighbor.id)) {
                    queue.add(neighbor);
                }
            }
        }
    }

    public static void main(String[] args) {
        String fileName = "assignment3/graphs1.txt"; // finds file 
        processGraphs(fileName);
    }
}
