// AVL.java acts as a custom implementation of the AVL Tree Data Structure 

public class AVL<T extends Comparable<T>> {
    // AVL tree node class
    private static class AVLnode<T> {
        AVLnode(T data) {
            this(data, null, null);
        }

        AVLnode(T data, AVLnode<T> left, AVLnode<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.height = 1; // A single node has height 1
        }

        // attributes of the AVL tree node class
        T data;
        AVLnode<T> left;
        AVLnode<T> right;
        int height;
    }

    private AVLnode<T> root;

    public AVL() {
        root = null;
    }

    // method to get the height from an int value (null height encoded as -1)
    // method to get the height of the node identified by key
    // Traverses from root and returns the height if key is found, otherwise -1
    public int getAVLKeyHeight(T key) {
        AVLnode<T> curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.data);
            if (cmp == 0)
                return curr.height;
            curr = (cmp < 0) ? curr.left : curr.right;
        }
        return -1;
    }

    // helper to safely get a node's height as int
    private int nodeHeight(AVLnode<T> node) {
        return (node == null) ? -1 : node.height;
    }

    // Note: all private helper functions are listed from here onwards:

    // AVL tree insertion method (maintains height balance after insertion &
    // deletion)
    public AVLnode<T> insert(T data, AVLnode<T> node) {
        if (node == null) {
            return new AVLnode<T>(data, null, null);
        }

        int compareResult = data.compareTo(node.data);
        if (compareResult < 0) {
            node.left = insert(data, node.left);
        } else if (compareResult > 0) {
            node.right = insert(data, node.right);
        } else {
            // Duplicate; do nothing
            return balance(node);
        }

        // rebalance and update heights
        return balance(node);
    }

    // AVL Tree lookup for a specific node
    public AVLnode<T> lookup(T data, AVLnode<T> node) {
        if (node == null) {
            return null;
        }

        int compareResult = data.compareTo(node.data);
        if (compareResult < 0) {
            return lookup(data, node.left);
        } else if (compareResult > 0) {
            return lookup(data, node.right);
        } else {
            return node; // Found, so we are returning the node
        }
    }

    // helper function to find minimum in subtree
    private AVLnode<T> findMin(AVLnode<T> node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private static final int BALANCE_FACTOR = 1;

    // helper function deployed right after insertion or deletion to balance the
    // tree
    private AVLnode<T> balance(AVLnode<T> node) {
        if (node == null) {
            return node;
        }

        if (nodeHeight(node.left) - nodeHeight(node.right) > BALANCE_FACTOR) {
            if (nodeHeight(node.left.left) >= nodeHeight(node.left.right)) {
                node = rotateWithLeftChild(node);
            } else {
                node = doubleWithLeftChild(node);
            }
        } else if (nodeHeight(node.right) - nodeHeight(node.left) > BALANCE_FACTOR) {
            if (nodeHeight(node.right.right) >= nodeHeight(node.right.left)) {
                node = rotateWithRightChild(node);
            } else {
                node = doubleWithRightChild(node);
            }
        }

        node.height = Math.max(nodeHeight(node.left), nodeHeight(node.right)) + 1;
        return node;
    }

    // rotate node with left child
    private AVLnode<T> rotateWithLeftChild(AVLnode<T> k2) {
        AVLnode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(nodeHeight(k2.left), nodeHeight(k2.right)) + 1;
        k1.height = Math.max(nodeHeight(k1.left), k2.height) + 1;
        return k1;
    }

    // double rotate node with left child
    private AVLnode<T> doubleWithLeftChild(AVLnode<T> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    // rotate node with right child
    private AVLnode<T> rotateWithRightChild(AVLnode<T> k1) {
        AVLnode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(nodeHeight(k1.left), nodeHeight(k1.right)) + 1;
        k2.height = Math.max(nodeHeight(k2.right), k1.height) + 1;
        return k2;
    }

    // double rotate node with right child
    private AVLnode<T> doubleWithRightChild(AVLnode<T> k3) {
        k3.right = rotateWithLeftChild(k3.right);
        return rotateWithRightChild(k3);
    }

    // deletion from a subtree
    private AVLnode<T> delete(T data, AVLnode<T> node) {
        if (node == null) {
            return node;
        }

        int compareResult = data.compareTo(node.data);
        if (compareResult < 0) {
            node.left = delete(data, node.left);
        } else if (compareResult > 0) {
            node.right = delete(data, node.right);
        } else if (node.left != null && node.right != null) {
            node.data = findMin(node.right).data;
            node.right = delete(node.data, node.right);
        } else {
            node = (node.left != null) ? node.left : node.right;
        }
        return balance(node);
    }

    public void insert(T data) {
        root = insert(data, root);
    }

    public boolean lookup(T data) {
        return lookup(data, root) != null;
    }

}
