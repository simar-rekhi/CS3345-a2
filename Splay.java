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
        // Base case: empty tree or found the key
        if (node == null || key.compareTo(node.data) == 0) {
            return node;
        }

        // Key should be in left subtree
        if (key.compareTo(node.data) < 0) {
            if (node.left == null) return node;
            
            if (key.compareTo(node.left.data) < 0) {
                // Zig-Zig case
                node.left.left = splay(key, node.left.left);
                node = rotateRight(node);
            } else if (key.compareTo(node.left.data) > 0) {
                // Zig-Zag case
                node.left.right = splay(key, node.left.right);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            
            if (node.left == null) return node;
            return rotateRight(node);
        } else {
            // Key should be in right subtree
            if (node.right == null) return node;
            
            if (key.compareTo(node.right.data) > 0) {
                // Zag-Zag case
                node.right.right = splay(key, node.right.right);
                node = rotateLeft(node);
            } else if (key.compareTo(node.right.data) < 0) {
                // Zag-Zig case
                node.right.left = splay(key, node.right.left);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            }
            
            if (node.right == null) return node;
            return rotateLeft(node);
        }
    }

    // single right rotation
    private SplayNode<T> rotateRight(SplayNode<T> node) {
        SplayNode<T> pivot = node.left;
        SplayNode<T> temp = pivot.right;
        pivot.right = node;
        node.left = temp;
        return pivot;
    }

    // single left rotation
    private SplayNode<T> rotateLeft(SplayNode<T> node) {
        SplayNode<T> pivot = node.right;
        SplayNode<T> temp = pivot.left;
        pivot.left = node;
        node.right = temp;
        return pivot;
    }

    // DFS traversal returning pre-order list of keys
    public java.util.List<T> DFSSplayTree() {
        java.util.List<T> out = new java.util.ArrayList<>();
        dfs(root, out);
        return out;
    }

    // pre-order recursive helper
    private void dfs(SplayNode<T> node, java.util.List<T> out) {
        if (node == null) return;
        out.add(node.data);
        dfs(node.left, out);
        dfs(node.right, out);
    }

    public void insert(T data) {
        if (root == null) {
            root = new SplayNode<>(data);
            return;
        }
        root = insert(data, root);
        // After insertion, splay the inserted node to root
        root = splay(data, root);
    }

    public boolean lookup(T data) {
        if (root == null) return false;
        root = splay(data, root);
        return root != null && root.data.equals(data);
    }

    // Test method
    public static void main(String[] args) {
        Splay<Integer> tree = new Splay<>();
        // Insert some numbers and check root after each operation
        tree.insert(9357);
        System.out.println("After inserting 9357, root=" + tree.root.data);
        System.out.println("DFS traversal: " + tree.DFSSplayTree());
    }
}
