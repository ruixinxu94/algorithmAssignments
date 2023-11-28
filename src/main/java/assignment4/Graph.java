package assignment4;
import java.io.*;
import java.util.*;


public class Graph {

    private final static String ERROR_IO = "error: failed to read or write files";

    private final static String ERROR_INPUT_FORMAT = "error: the format of the input line is wrong";

    private final static String HELP_MSG = "Usage: java Graph <input file name> <number of nodes> " +
            "<starting node> <output file name>";
    private static Node startingNode;

    private static Map<Integer, Node> map;

    private static String BFS = "BFS";

    private static String DFS = "DFS";

    List<int[]> dfsResult = new ArrayList<>();

    List<int[]> bfsResult = new ArrayList<>();

    static class Node {
        Node(int val) {
            this.val = val;
        }

        int val;
        Set<Node> neighbours = new LinkedHashSet<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return val == node.val;
        }

        @Override
        public int hashCode() {
            return Objects.hash(val);
        }
    }

    public void dfs(Node startNode) {
        if (startNode == null) {
            return;
        }
        // System.out.println("Starting DFS from node: " + startNode.val);
        Set<Node> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        visited.add(startNode);
        stack.push(startNode);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            // System.out.println("Processing node: " + currentNode.val);
            // boolean foundUnvisitedNeighbor = false;
            for (Node neighbour : currentNode.neighbours) {
                if (!visited.contains(neighbour)) {
                    // Found an unvisited neighbor, push the current node back
                    stack.push(currentNode);
                    visited.add(neighbour);
                    stack.push(neighbour);

                    int[] arr = new int[]{Math.min(neighbour.val, currentNode.val),
                            Math.max(neighbour.val, currentNode.val)};
                    // System.out.println("Adding edge: " + arr[0] + " - " + arr[1]);
                    dfsResult.add(arr);
                    // System.out.println("Pushing neighbor to stack: " + neighbour.val);
                    // System.out.println();
                    // foundUnvisitedNeighbor = true;
                    break;
                }
            }
/*            if (!foundUnvisitedNeighbor) {
                System.out.println();
                System.out.println("Backtracking from node: " + currentNode.val);
                System.out.println();
            }*/
        }
    }


    void bfs(Node startNode) {
        if (startNode == null) {
            return;
        }
        Set<Node> visited = new HashSet<>();
        Deque<Node> queue = new LinkedList<>();
        visited.add(startNode);
        queue.offer(startNode);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            System.out.println("Visiting node: " + currentNode.val);
            for (Node neighbour : currentNode.neighbours) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.offer(neighbour);
                    int[] arr = new int[]{Math.min(neighbour.val, currentNode.val),
                            Math.max(neighbour.val, currentNode.val)};
                    System.out.println("Adding edge: " + arr[0] + " - " + arr[1]);
                    bfsResult.add(arr);
                }
            }
        }
    }


    private Node findOrCreateNode(int val) {
        if (map.containsKey(val)) {
            return map.get(val);
        }
        Node newNode = new Node(val);
        map.put(val, newNode);
        return newNode;
    }

    boolean convertToMap(String filePath) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
        ) {
            String line = reader.readLine();
            while (line != null) {
                String[] array = line.split(" ");
                if (array.length != 2) {
                    System.out.println(ERROR_INPUT_FORMAT);
                    return false;
                }
                int val = Integer.parseInt(array[0]);
                int edge = Integer.parseInt(array[1]);
                Node node = findOrCreateNode(val);
                Node edgeNode = findOrCreateNode(edge);
                if (!node.neighbours.contains(edgeNode)) {
                    node.neighbours.add(edgeNode);
                }
                if (!edgeNode.neighbours.contains(node)) {
                    edgeNode.neighbours.add(node);
                }
                map.put(val, node);
                map.put(edge, edgeNode);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(ERROR_IO);
            return false;
        }
        return true;
    }

    boolean outputFile(String filePath, List<int[]> list, String algorithmName, boolean isAppending) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, isAppending))) {
            writer.write(algorithmName + ":");
            writer.newLine();
            for (int[] pair : list) {
                writer.write(pair[0] + " " + pair[1]);
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println(ERROR_IO);
            return false;
        }
        return true;
    }

    public void printMap() {
        for (Map.Entry<Integer, Node> entry : map.entrySet()) {
            Node node = entry.getValue();
            System.out.print("Node " + node.val + " has neighbors: ");
            for (Node neighbour : node.neighbours) {
                System.out.print(neighbour.val + " ");
            }
            System.out.println(); // Move to the next line after printing all neighbors
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length != 4 || args[0].equals("--help") || args[0].equals("-h")) {
            System.out.println(HELP_MSG);
            return;
        }
        Graph graph = new Graph();
        int numberOfNodes = Integer.parseInt(args[1]);
        int startingNodeValue = Integer.parseInt(args[2]);
        String intputFilePath = args[0];
        String outputFilePath = args[3];
        map = new HashMap<>(numberOfNodes);
        if (!graph.convertToMap(intputFilePath)) {
            return;
        }
        graph.printMap();
        startingNode = graph.findOrCreateNode(startingNodeValue);
        graph.bfs(startingNode);
        graph.dfs(startingNode);
        if (!graph.outputFile(outputFilePath, graph.bfsResult, BFS, false)) {
            return;
        }
        graph.outputFile(outputFilePath, graph.dfsResult, DFS, true);
    }
}

