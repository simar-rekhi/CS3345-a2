// Splay.java acts as a custom implementation of the Splay Tree Data Structure 

public class Splay<T extends Comparable<T>> {

    // Splay tree node class
    public static class SplayNode<T> {
        SplayNode(T data) {
            this(data, null, null);
        }

        public SplayNode(T data, SplayNode<T> left, SplayNode<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        // attributes of the Splay tree node class
        T data;
        SplayNode<T> left;
        SplayNode<T> right;
    }

    private SplayNode<T> root;

    public Splay() {
        root = null;
    }

    // Splay Tree insertion method - simply inserts, splaying is done as a separate
    // operation, to modularize the code
    private SplayNode<T> insert(T data, SplayNode<T> node) {
        if (node == null) {
            return new SplayNode<T>(data, null, null);
        }

        int compareResult = data.compareTo(node.data);
        if (compareResult < 0) {
            node.left = insert(data, node.left);
        } else if (compareResult > 0) {
            node.right = insert(data, node.right);
        } else {
            // Duplicate; do nothing
            return node;
        }
        return node;
    }

    // Splay operation to bring the node with the given key to the root
    private SplayNode<T> splay(T key, SplayNode<T> node) {
        if (node == null) {
            return null;
        }

        int compareResult = key.compareTo(node.data);
        if (compareResult < 0) {
            // Key lies in left subtree
            if (node.left == null) {
                return node; // Key not found
            }
            int leftCompare = key.compareTo(node.left.data);
            if (leftCompare < 0) {
                // Zig-Zig (Left Left)
                node.left.left = splay(key, node.left.left);
                node = rotateRight(node);
            } else if (leftCompare > 0) {
                // Zig-Zag (Left Right)
                node.left.right = splay(key, node.left.right);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            return (node.left == null) ? node : rotateRight(node);
        } else if (compareResult > 0) {
            // Key lies in right subtree
            if (node.right == null) {
                return node; // Key not found
            }
            int rightCompare = key.compareTo(node.right.data);
            if (rightCompare > 0) {
                // Zag-Zag (Right Right)
                node.right.right = splay(key, node.right.right);
                node = rotateLeft(node);
            } else if (rightCompare < 0) {
                // Zag-Zig (Right Left)
                node.right.left = splay(key, node.right.left);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            }
            return (node.right == null) ? node : rotateLeft(node);
        } else {
            return node; // Key found
        }
    }

    // single right rotation
    private SplayNode<T> rotateRight(SplayNode<T> y) {
        SplayNode<T> x = y.left;
        y.left = x.right;
        x.right = y;
        return x;
    }

    // single left rotation
    private SplayNode<T> rotateLeft(SplayNode<T> x) {
        SplayNode<T> y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    // Lookup Operation - implementing DFS to print all the keys in splay tree
    public void DFSSplayTree() {
        if (root == null) {
            System.out.println("Splay Tree is empty.");
        } else {
            System.out.print("Splay Tree Lookup: \n");
            // Iterative DFS using a stack
            java.util.Deque<SplayNode<T>> stack = new java.util.ArrayDeque<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                SplayNode<T> node = stack.pop();
                System.out.println(node.data);
                // push right first so left is processed next (pre-order)
                if (node.right != null)
                    stack.push(node.right);
                if (node.left != null)
                    stack.push(node.left);
            }
        }
    }

    public void insert(T data) {
        root = insert(data, root);
    }

    public boolean lookup(T data) {
        root = splay(data, root);
        return root != null && root.data.equals(data);
    }
}
