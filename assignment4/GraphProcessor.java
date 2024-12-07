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

    public static void processGraphs(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isNewGraph = false;
            Map<String, Vertex> linkedGraph = new HashMap<>();

            System.out.println("SSSP Results for Graphs");

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("new graph")) {
                    if (isNewGraph) {
                        System.out.println("\nGraph:");
                        runSSSP(linkedGraph, "1");
                    }
                    isNewGraph = true;
                    linkedGraph.clear();
                } else if (line.startsWith("add vertex")) {
                    String vertexId = line.replace("add vertex", "").trim();
                    linkedGraph.put(vertexId, new Vertex(vertexId));
                } else if (line.startsWith("add edge")) {
                    String[] parts = line.replace("add edge", "").trim().split("\\s+");

                    if (parts.length < 4) {
                        System.err.println("Invalid edge format: " + line);
                        continue;
                    }

                    String u = parts[0];
                    String v = parts[2];
                    int weight = Integer.parseInt(parts[parts.length - 1]);

                    if (linkedGraph.containsKey(u) && linkedGraph.containsKey(v)) {
                        linkedGraph.get(u).addEdge(linkedGraph.get(v), weight);
                    } else {
                        System.err.println("Invalid edge with undefined vertices: " + line);
                    }
                }
            }

            if (isNewGraph) {
                System.out.println("\nGraph:");
                runSSSP(linkedGraph, "1");
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public static void runSSSP(Map<String, Vertex> graph, String sourceId) {
        if (!graph.containsKey(sourceId)) {
            System.out.println("Source vertex " + sourceId + " not found in the graph.");
            return;
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();

        for (String vertexId : graph.keySet()) {
            distances.put(vertexId, Integer.MAX_VALUE);
            predecessors.put(vertexId, null);
        }
        distances.put(sourceId, 0);

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

        for (Vertex vertex : graph.values()) {
            for (Edge edge : vertex.edges) {
                if (distances.get(vertex.id) != Integer.MAX_VALUE
                        && distances.get(vertex.id) + edge.weight < distances.get(edge.to.id)) {
                    System.out.println("Graph contains a negative-weight cycle.");
                    return;
                }
            }
        }

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

    private static String getPath(Map<String, String> predecessors, String target) {
        List<String> path = new ArrayList<>();
        for (String at = target; at != null; at = predecessors.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return String.join(" --> ", path);
    }

    static class Spice {
        String name;
        double totalPrice;
        int quantity;
        double unitPrice;

        public Spice(String name, double totalPrice, int quantity) {
            this.name = name;
            this.totalPrice = totalPrice;
            this.quantity = quantity;
            this.unitPrice = totalPrice / quantity;
        }
    }

    static class Knapsack {
        int capacity;

        public Knapsack(int capacity) {
            this.capacity = capacity;
        }
    }

    public static void processSpice(String filename) {
        List<Spice> spices = new ArrayList<>();
        List<Knapsack> knapsacks = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("spice name")) {
                    String[] parts = line.split(";");
                    String name = parts[0].split("=")[1].trim();
                    double totalPrice = Double.parseDouble(parts[1].split("=")[1].trim());
    
                    // Remove all non-digit characters from the quantity string (e.g., trailing semicolon)
                    String quantityStr = parts[2].split("=")[1].trim().replaceAll("[^\\d]", "");
                    int quantity = Integer.parseInt(quantityStr);
    
                    spices.add(new Spice(name, totalPrice, quantity));
                } else if (line.startsWith("knapsack capacity")) {
                    // Parse knapsack capacity
                    String capacityStr = line.split("=")[1].trim().replaceAll("[^\\d]", "");
                    int capacity = Integer.parseInt(capacityStr);
                    knapsacks.add(new Knapsack(capacity));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing a number: " + e.getMessage());
            return;
        }
    
        // Sort spices by unit price in descending order
        spices.sort((a, b) -> Double.compare(b.unitPrice, a.unitPrice));
    
        // Process each knapsack
        for (Knapsack knapsack : knapsacks) {
            int remainingCapacity = knapsack.capacity;
            double totalValue = 0;
            List<String> contents = new ArrayList<>();
    
            for (Spice spice : spices) {
                if (remainingCapacity <= 0) break;
    
                int takeQuantity = Math.min(remainingCapacity, spice.quantity);
                totalValue += takeQuantity * spice.unitPrice;
                remainingCapacity -= takeQuantity;
    
                if (takeQuantity > 0) {
                    contents.add(takeQuantity + " scoop(s) of " + spice.name);
                }
            }
    
            // Output the result for the current knapsack
            System.out.println("Knapsack of capacity " + knapsack.capacity + " is worth " + totalValue + " quatloos and contains " + String.join(", ", contents) + ".");
        }
    }

    public static void main(String[] args) {
        String graphFileName = "assignment4/graphs2.txt";
        processGraphs(graphFileName);

        String spiceFileName = "assignment4/spice.txt";
        System.out.println("\nSpice Heist Results:");
        processSpice(spiceFileName);
    }
}
