package assignment4;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GraphTest {

    private Graph graph;
    @Test
    public void testSimpleGraphDFS() {
        // Creating a simple graph
        // Let's say we have a graph like: 1 -- 2 -- 3
        graph = new Graph();
        Graph.Node node1 = new Graph.Node(1);
        Graph.Node node2 = new Graph.Node(2);
        Graph.Node node3 = new Graph.Node(3);

        node1.neighbours.add(node2);
        node2.neighbours.add(node1);
        node2.neighbours.add(node3);
        node3.neighbours.add(node2);

        graph.dfs(node1);

        // Expected DFS traversal: [(1,2), (2,3)]
        List<int[]> expectedDFSResult = new ArrayList<>();
        expectedDFSResult.add(new int[]{1, 2});
        expectedDFSResult.add(new int[]{2, 3});

        assertEquals(expectedDFSResult.size(), graph.dfsResult.size());
        for (int i = 0; i < expectedDFSResult.size(); i++) {
            assertArrayEquals(expectedDFSResult.get(i), graph.dfsResult.get(i));
        }
    }

//    @Test
//    public void testSimpleGraphBFS() {
//        // Reusing the same simple graph setup from DFS test
//        graph = new Graph();
//        graph.bfs(new Graph.Node(1));
//
//        // Expected BFS traversal: [(1,2), (2,3)]
//        List<int[]> expectedBFSResult = new ArrayList<>();
//        expectedBFSResult.add(new int[]{1, 2});
//        expectedBFSResult.add(new int[]{2, 3});
//
//        assertEquals(expectedBFSResult.size(), graph.bfsResult.size());
//        for (int i = 0; i < expectedBFSResult.size(); i++) {
//            assertArrayEquals(expectedBFSResult.get(i), graph.bfsResult.get(i));
//        }
//    }

    @Test
    public void testDisconnectedGraphDFS() {
        // Graph structure: 1 -- 2, 3 -- 4 (Two disconnected components)
        graph = new Graph();
        Graph.Node node1 = new Graph.Node(1);
        Graph.Node node2 = new Graph.Node(2);
        Graph.Node node3 = new Graph.Node(3);
        Graph.Node node4 = new Graph.Node(4);

        node1.neighbours.add(node2);
        node2.neighbours.add(node1);
        node3.neighbours.add(node4);
        node4.neighbours.add(node3);

        graph.dfs(node1);

        // Expected DFS traversal starting from node 1: [(1,2)]
        List<int[]> expectedDFSResult = new ArrayList<>();
        expectedDFSResult.add(new int[]{1, 2});

        assertEquals(expectedDFSResult.size(), graph.dfsResult.size());
        for (int i = 0; i < expectedDFSResult.size(); i++) {
            assertArrayEquals(expectedDFSResult.get(i), graph.dfsResult.get(i));
        }
    }

    @Test
    public void testGraphWithCycleDFS() {
        // Graph with a cycle: 1 -- 2 -- 3 -- 1
        graph = new Graph();
        Graph.Node node1 = new Graph.Node(1);
        Graph.Node node2 = new Graph.Node(2);
        Graph.Node node3 = new Graph.Node(3);

        node1.neighbours.add(node2);
        node2.neighbours.add(node1);
        node2.neighbours.add(node3);
        node3.neighbours.add(node2);
        node3.neighbours.add(node1);
        node1.neighbours.add(node3);

        graph.dfs(node1);

        List<int[]> expectedDFSResult1 = new ArrayList<>();
        expectedDFSResult1.add(new int[]{1, 2});
        expectedDFSResult1.add(new int[]{2, 3});

        assertTrue(graph.dfsResult.containsAll(expectedDFSResult1));
    }


    @Test
    public void testEmptyGraphDFS() {
        graph = new Graph();
        graph.dfs(null);

        // Expected result for an empty graph: empty list
        assertTrue(graph.dfsResult.isEmpty());
    }

    @Test
    public void testSingleGraphNodeDFS() {
        graph = new Graph();
        // Graph with a single node
        Graph.Node node1 = new Graph.Node(1);

        graph.dfs(node1);

        // Expected DFS traversal: empty list as there are no edges
        assertTrue(graph.dfsResult.isEmpty());
    }






}
