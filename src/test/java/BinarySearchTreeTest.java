import assignment2.BinarySearchTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BinarySearchTreeTest {

    private BinarySearchTree bst;

    @BeforeEach
    public void setUp() {
        bst = new BinarySearchTree();
    }

    @Test
    public void testInsert() {
        assertTrue(bst.insert("apple", 10));
        assertTrue(bst.insert("banana", 20));
        assertFalse(bst.insert("apple", 30));  // Duplicates are not allowed
    }

    @Test
    public void testSearch() {
        bst.insert("apple", 10);
        bst.insert("banana", 20);

        BinarySearchTree.Node node = bst.search("apple");
        assertNotNull(node);
        assertEquals("apple", node.name);
        assertEquals(10, node.count);

        node = bst.search("banana");
        assertNotNull(node);
        assertEquals("banana", node.name);
        assertEquals(20, node.count);

        node = bst.search("cherry");
        assertNull(node);  // Not found
    }

    @Test
    public void testDelete() {
        bst.insert("apple", 10);
        bst.insert("banana", 20);

        BinarySearchTree.Node node = bst.delete("apple");
        assertNotNull(node);
        assertEquals("apple", node.name);
        assertEquals(10, node.count);

        node = bst.search("apple");
        assertNull(node);  // Not found

        node = bst.delete("banana");
        assertNotNull(node);
        assertEquals("banana", node.name);
        assertEquals(20, node.count);

        node = bst.search("banana");
        assertNull(node);  // Not found

        node = bst.delete("cherry");
        assertNull(node);  // Not found
    }

    @Test
    public void testDepthVerification() {
        assertTrue(bst.insert("apple", 10));
        assertEquals(1, bst.getTempDepth());  // Depth after inserting root node

        assertTrue(bst.insert("banana", 20));
        assertEquals(2, bst.getTempDepth());  // Depth after inserting a node at depth 2

        assertTrue(bst.insert("cherry", 30));
        assertEquals(3, bst.getTempDepth());  // Depth after inserting a node at depth 3

        bst.search("banana");
        assertEquals(2, bst.getTempDepth());  // Depth after searching a node at depth 2

        assertNotNull(bst.delete("banana"));
        assertEquals(2, bst.getTempDepth());  // Depth after deleting a node at depth 2
    }


    @Test
    public void testDeleteNodeWithTwoChildren() {
        bst.insert("apple", 10);
        bst.insert("banana", 20);
        bst.insert("cherry", 30);

        BinarySearchTree.Node node = bst.delete("banana");
        assertNotNull(node);
        assertEquals("banana", node.name);
        assertEquals(20, node.count);

        // Verify tree restructure by searching for the successor
        BinarySearchTree.Node successor = bst.search("cherry");
        assertNotNull(successor);
        assertEquals("cherry", successor.name);
    }

    @Test
    public void testLargeInputScenario() {
        for (int i = 0; i < 1000; i++) {
            assertTrue(bst.insert("node" + i, i));
        }

        for (int i = 0; i < 1000; i++) {
            BinarySearchTree.Node node = bst.search("node" + i);
            assertNotNull(node);
            assertEquals("node" + i, node.name);
            assertEquals(i, node.count);
        }
    }

}
