package assignment5;

public class DijkstrasAlgorithm {

    // Number of vertices in the graph
    private static final int V = 5;

    // Function to find the vertex with the minimum distance value from the set of vertices not yet processed
    int minDistance(int[] dist, Boolean[] sptSet) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < V; v++) {
            if (!sptSet[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    // Utility function to print the constructed distance array
    void printSolution(int[] dist) {
        System.out.println("Vertex \t Distance from Source");
        for (int i = 0; i < V; i++)
            System.out.println(i + " \t\t " + dist[i]);
    }

    // Function to print shortest path from source to j using parent array
    void printPath(int[] parent, int j) {
        // Base Case : If j is source
        if (parent[j] == -1)
            return;

        printPath(parent, parent[j]);
        System.out.print(j + " ");
    }

    // Function that implements Dijkstra's algorithm for a graph represented using an adjacency matrix
    void dijkstra(int[][] graph) {
        int[] dist = new int[V];
        Boolean[] sptSet = new Boolean[V];
        int[] parent = new int[V]; // Array to store constructed path

        for (int i = 0; i < V; i++) {
            parent[i] = -1; // Initialize parent array
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        dist[0] = 0;

        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, sptSet);
            sptSet[u] = true;

            for (int v = 0; v < V; v++) {
                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) {
                    parent[v] = u;
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }

        printSolution(dist);
        System.out.println("Paths from source to all vertices:");
        for (int i = 0; i < V; i++) {
            if (i != 0) {
                System.out.print("Path from " + 0 + " to " + i + ": " + 0 + " ");
                printPath(parent, i);
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        DijkstrasAlgorithm t = new DijkstrasAlgorithm();
        int[][] graph = new int[][]{
                {0, 2, 0, 6, 0},
                {2, 0, 3, 8, 5},
                {0, 3, 0, 0, 7},
                {6, 8, 0, 0, 9},
                {0, 5, 7, 9, 0},
        };

        t.dijkstra(graph); // 0 is the source vertex
    }
}
