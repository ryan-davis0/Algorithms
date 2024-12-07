import java.io.*;
import java.util.*;

public class GraphProcessor {

    // Class to represent a vertex (node) in the graph
    static class Vertex {
        String id; // Identifier for the vertex
        List<Edge> edges; // List of edges originating from this vertex

        // Constructor to initialize a vertex with an ID
        Vertex(String id) {
            this.id = id;
            this.edges = new ArrayList<>();
        }

        // Method to add an edge from this vertex to another
        void addEdge(Vertex to, int weight) {
            edges.add(new Edge(this, to, weight));
        }
    }

    // Class to represent an edge in the graph
    static class Edge {
        Vertex from; // Starting vertex of the edge
        Vertex to;   // Ending vertex of the edge
        int weight;  // Weight of the edge

        // Constructor to initialize an edge
        Edge(Vertex from, Vertex to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    /**
     * Processes the graph file and constructs a linked object representation for directed, weighted graphs.
     * The graph file contains commands to add vertices and edges.
     * @param fileName Path to the input file containing graph data
     */
    public static void processGraphs(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line; // Stores the current line read from the file
            boolean isNewGraph = false; // Flag to indicate the start of a new graph
            Map<String, Vertex> linkedGraph = new HashMap<>(); // Representation of the graph as vertices and edges

            System.out.println("SSSP Results for Graphs");

            // Read file line by line
            while ((line = br.readLine()) != null) {
                line = line.trim(); // Remove unnecessary whitespace

                // Check if the line indicates a new graph
                if (line.startsWith("new graph")) {
                    if (isNewGraph) {
                        System.out.println("\nGraph:");
                        runSSSP(linkedGraph, "1"); // Run Single Source Shortest Path (SSSP) for the current graph
                    }
                    isNewGraph = true;
                    linkedGraph.clear(); // Clear the graph representation for the new graph
                }
                // Check if the line adds a new vertex
                else if (line.startsWith("add vertex")) {
                    String vertexId = line.replace("add vertex", "").trim();
                    linkedGraph.put(vertexId, new Vertex(vertexId)); // Add the vertex to the graph
                }
                // Check if the line adds a new edge
                else if (line.startsWith("add edge")) {
                    // Parse the edge input: "add edge 2 - 5 -4"
                    String[] parts = line.replace("add edge", "").trim().split("\\s+");

                    // Validate edge format
                    if (parts.length < 4) {
                        System.err.println("Invalid edge format: " + line);
                        continue;
                    }

                    String u = parts[0]; // Source vertex
                    String v = parts[2]; // Destination vertex
                    int weight = Integer.parseInt(parts[parts.length - 1]); // Edge weight

                    // Add edge if vertices exist in the graph
                    if (linkedGraph.containsKey(u) && linkedGraph.containsKey(v)) {
                        linkedGraph.get(u).addEdge(linkedGraph.get(v), weight);
                    } else {
                        System.err.println("Invalid edge with undefined vertices: " + line);
                    }
                }
            }

            // Process the last graph if it exists
            if (isNewGraph) {
                System.out.println("\nGraph:");
                runSSSP(linkedGraph, "1"); // Run SSSP for the last graph
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    /**
     * Runs the Single Source Shortest Path (SSSP) algorithm using the Bellman-Ford method.
     * Prints the shortest path costs and paths from a source vertex to all other vertices.
     * @param graph The graph represented as a map of vertex IDs to Vertex objects
     * @param sourceId The ID of the source vertex
     */
    public static void runSSSP(Map<String, Vertex> graph, String sourceId) {
        // Check if the source vertex exists in the graph
        if (!graph.containsKey(sourceId)) {
            System.out.println("Source vertex " + sourceId + " not found in the graph.");
            return;
        }

        Map<String, Integer> distances = new HashMap<>(); // Stores shortest distances from the source
        Map<String, String> predecessors = new HashMap<>(); // Stores predecessors for path reconstruction

        // Initialize distances and predecessors
        for (String vertexId : graph.keySet()) {
            distances.put(vertexId, Integer.MAX_VALUE); // Set all distances to infinity
            predecessors.put(vertexId, null); // Set no predecessor initially
        }
        distances.put(sourceId, 0); // Distance to the source is 0

        // Perform multiple passes through all vertices to update paths
        int verticesCount = graph.size();
        for (int i = 1; i < verticesCount; i++) {
            for (Vertex vertex : graph.values()) {
                for (Edge edge : vertex.edges) {
                    if (distances.get(vertex.id) != Integer.MAX_VALUE
                            && distances.get(vertex.id) + edge.weight < distances.get(edge.to.id)) {
                        distances.put(edge.to.id, distances.get(vertex.id) + edge.weight);
                        predecessors.put(edge.to.id, vertex.id);
                    }
                }
            }
        }

        // Check for negative-weight cycles
        for (Vertex vertex : graph.values()) {
            for (Edge edge : vertex.edges) {
                if (distances.get(vertex.id) != Integer.MAX_VALUE
                        && distances.get(vertex.id) + edge.weight < distances.get(edge.to.id)) {
                    System.out.println("Graph contains a negative-weight cycle.");
                    return;
                }
            }
        }

        // Print results for all vertices except the source
        for (String vertexId : graph.keySet()) {
            if (!vertexId.equals(sourceId)) {
                if (distances.get(vertexId) == Integer.MAX_VALUE) {
                    System.out.println("No path from " + sourceId + " to " + vertexId);
                } else {
                    System.out.println("1 --> " + vertexId + " cost is " + distances.get(vertexId) +
                            "; path: " + getPath(predecessors, vertexId));
                }
            }
        }
    }

    /**
     * Reconstructs the shortest path from the predecessor map.
     * @param predecessors Map of vertex IDs to their predecessors
     * @param target The target vertex ID
     * @return A string representation of the path
     */
    private static String getPath(Map<String, String> predecessors, String target) {
        List<String> path = new ArrayList<>();
        for (String at = target; at != null; at = predecessors.get(at)) {
            path.add(at);
        }
        Collections.reverse(path); // Reverse to get the correct order
        return String.join(" --> ", path);
    }

    public static void main(String[] args) {
        String graphFileName = "assignment4/graphs2.txt"; // Input file containing graph data
        processGraphs(graphFileName); // Start processing the graph file
    }
}
