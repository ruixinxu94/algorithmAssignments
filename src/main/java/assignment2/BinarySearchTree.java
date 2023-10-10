package assignment2;

import java.io.*;

/**
 * @author Ruixin Xu
 * @apiNote java assignment2.BinarySearchTree <input file name> <output file name>
 * java assignment2.BinarySearchTree src/main/java/textdata.txt src/main/java/output.txt
 * Consider an Entry object to be composed of two parts, a name and a count. The goal is to store such entries
 * in a dictionary, treating name as key and count as value.
 */
public class BinarySearchTree {

    private final static String ACTION_ADD = "ADD";
    private final static String ACTION_SEARCH = "SEARCH";
    private final static String ACTION_DELETE = "DELETE";
    private final static String HELP_MSG = "Usage: java assignment2.BinarySearchTree <input file name> <output file name>";

    private final static String ERROR_INPUT_FORMAT = "error: the format of the input line is wrong";
    private final static String ERROR_PARSE_INT = "error: failed to parse string to number";
    private final static String ERROR_ACTION = "error: failed to get the correct action";
    private final static String ERROR_IO = "error: failed to read or write files";

    private Node root;
    private int tempDepth;

    public int getTempDepth() {
        return tempDepth;
    }

    private void resetDepth() {
        this.tempDepth = 0;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public static class Node {
        Node parent;
        Node left;
        Node right;
        public int count;
        public String name;

        Node(String name, int count) {
            this.name = name;
            this.count = count;
        }
    }

    public Node search(String name) {
        // this part could be removed
        if (root == null) {
            return null;
        }
        resetDepth();
        if (root.name.equals(name)) {
            tempDepth++;
            return root;
        }
        Node currentNode = root;
        while (currentNode != null) {
            tempDepth++;
            String currentName = currentNode.name;
            if (currentName.equals(name)) {
                return currentNode;
            } else if (currentName.compareTo(name) > 0) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        return null;
    }

    public boolean insert(String name, int count) {
        resetDepth();
        if (root == null) {
            root = new Node(name, count);
            tempDepth++;
            return true;
        }
        Node parent = null;
        Node currentNode = root;
        while (currentNode != null) {
            String currentName = currentNode.name;
            if (currentName.equals(name)) {
                return false;
            } else if (currentName.compareTo(name) > 0) {
                parent = currentNode;
                currentNode = currentNode.left;
            } else {
                parent = currentNode;
                currentNode = currentNode.right;
            }
            tempDepth++;
        }
        currentNode = new Node(name, count);
        appendNodeToNewParent(currentNode, parent);
        tempDepth++;
        return true;
    }

    public Node delete(String name) {
        resetDepth();
        if (root == null) {
            return null;
        }
        Node currentNode = search(name);
        if (currentNode == null) {
            return null;
        }
        Node resultNode = new Node(currentNode.name, currentNode.count);
        Node parent = currentNode.parent;
        if (currentNode.left == null && currentNode.right == null) {
            // Node with no child
            detachNodeFromParent(currentNode);
            appendNodeToNewParent(null, parent);
        } else if (currentNode.left == null || currentNode.right == null) {
            // Node with only one child
            Node newChild = (currentNode.left == null) ? currentNode.right : currentNode.left;
            detachNodeFromParent(currentNode);
            appendNodeToNewParent(newChild, parent);
        } else {
            // Node with 2 children
            findAndReplaceWithSuccessor(currentNode);
        }
        return resultNode;
    }

    private void findAndReplaceWithSuccessor(Node currentNode) {
        if (currentNode == null) {
            return;
        }
        Node successorParent = currentNode;
        Node successor = currentNode.right;
        while (successor.left != null) {
            successorParent = successor;
            successor = successor.left;
        }
        currentNode.name = successor.name;
        currentNode.count = successor.count;
        if (successorParent != currentNode) {
            // The successor node is a left child of its parent
            successorParent.left = successor.right;
        } else {
            // The successor node is a right child of its parent
            successorParent.right = successor.right;
        }
    }

    private void detachNodeFromParent(Node node) {
        if (node == null) {
            setRoot(null);
            return;
        }
        Node parent = node.parent;
        if (parent == null) {
            root = null;
        } else if (node.parent.left == node) {
            node.parent.left = null;
        } else {
            node.parent.right = null;
        }
    }

    private void appendNodeToNewParent(Node currentNode, Node newParent) {
        if (currentNode == null) {
            return;
        }
        if (newParent == null) {
            root = currentNode;
            currentNode.parent = null;
            return;
        }
        if (currentNode.name.compareTo(newParent.name) < 0) {
            newParent.left = currentNode;
        } else {
            newParent.right = currentNode;
        }

        currentNode.parent = newParent;
    }

    private void readAndWriteLineByLine(String inputFile, String outputFile) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))
        ) {
            String line = reader.readLine();
            while (line != null) {
                String[] array = line.split(" ");
                if (array.length != 2) {
                    System.out.println(ERROR_INPUT_FORMAT);
                    return;
                }
                String actionName = array[0];
                String name;
                Integer count;
                if (actionName.equalsIgnoreCase(ACTION_ADD)) {
                    String[] nameAndCount = array[1].split(",");
                    name = nameAndCount[0];
                    count = tryParseInt(nameAndCount[1]);
                    if (count == null) {
                        System.out.println(ERROR_PARSE_INT);
                        return;
                    }
                    if (insert(name, count)) {
                        line = "Added " + "(" + name + "," + count + ") " + "at depth " + getTempDepth() + ".";
                    } else {
                        line = "Cannot add: " + name + ".";
                    }
                } else if (actionName.equalsIgnoreCase(ACTION_SEARCH)) {
                    name = array[1];
                    Node node = search(name);
                    if (node == null) {
                        line = "Not found: " + name + ".";
                    } else {
                        line = "Found " + "(" + name + "," + node.count + ") " + "at depth " + getTempDepth() + ".";
                    }
                } else if (actionName.equalsIgnoreCase(ACTION_DELETE)) {
                    name = array[1];
                    Node node = delete(name);
                    if (node == null) {
                        line = "Cannot delete: " + name + ".";
                    } else {
                        line = "Deleted " + "(" + name + "," + node.count + ").";
                    }
                } else {
                    System.out.println(ERROR_ACTION);
                    return;
                }
                writer.write(line);
                writer.newLine();
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(ERROR_IO);
        }
    }

    private Integer tryParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public static void doProcess(String[] args) {
        if (args == null || args.length != 2 || "--help".equals(args[0]) || "-h".equals(args[0])) {
            System.out.println(HELP_MSG);
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];
        BinarySearchTree binarySearchTree = new BinarySearchTree();
        binarySearchTree.readAndWriteLineByLine(inputFile, outputFile);
    }

    public static void main(String[] args) {
        doProcess(args);
    }

}
