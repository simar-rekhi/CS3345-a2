// AVL.java acts as a custom implementation of the AVL Tree Data Structure 


public class AVL<T extends Comparable<T>> {
    // AVL tree node class
    private static class AVLnode<T>{
        AVLnode(T data){
            this(data, null, null);
        }

        AVLnode(T data, AVLnode<T> left, AVLnode<T> right){
            this.data = data;
            this.left = left;
            this.right = right;
            this.height = 0;
        }

        // attributes of the AVL tree node class
        T data;
        AVLnode<T> left;
        AVLnode<T> right;
        int height;
    }
    
    private AVLnode<T> root;

    public AVL(){
        root = null;
    }

    // method to get the height of the tree
    public int getAVLKeyHeight(AVLnode<T> node){
        return (node == null) ? -1 : node.height;
    }

    // Note: all priv helper functions are listed from here onwards:

    // AVL tree insertion method (maintains height balance after insertion & deletion)
    public AVLnode<T> insert(T data, AVLnode<T> node){
        if (node == null){
            return new AVLnode<T>(data, null, null);
        }

        int compareResult = data.compareTo(node.data);
        if (compareResult < 0){
            node.left = insert(data, node.left);
        } else if (compareResult > 0){
            node.right = insert(data, node.right);
        } else {
            // Duplicate; do nothing
            return balance(node);
        }
        // rebalance and update heights
        return balance(node);
    }

    // AVL Tree lookup for a specific node
    public AVLnode<T> lookup(T data, AVLnode<T> node){
        if (node == null){
            return null;
        }

        int compareResult = data.compareTo(node.data);
        if (compareResult < 0){
            return lookup(data, node.left);
        } else if (compareResult > 0){
            return lookup(data, node.right);
        } else {
            return node; // Found, so we are returning the node
        }
    }

    // helper function to find minimum in subtree
    private AVLnode<T> findMin(AVLnode<T> node){
        if (node == null){
            return null;
        }
        while (node.left != null){
            node = node.left;
        }
        return node;
    }

    private static final int BALANCE_FACTOR = 1;


    // helper function deployed right after insertion or deletion to balance the tree and maintain AVL tree conditions
    private AVLnode<T> balance(AVLnode<T> node){
        if (node == null){
            return node;
        }

        if (getAVLKeyHeight(node.left) - getAVLKeyHeight(node.right) > BALANCE_FACTOR){
            if (getAVLKeyHeight(node.left.left) >= getAVLKeyHeight(node.left.right)){
                node = rotateWithLeftChild(node);
            } else {
                node = doubleWithLeftChild(node);
            }
        } else if (getAVLKeyHeight(node.right) - getAVLKeyHeight(node.left) > BALANCE_FACTOR){
            if (getAVLKeyHeight(node.right.right) >= getAVLKeyHeight(node.right.left)){
                node = rotateWithRightChild(node);
            } else {
                node = doubleWithRightChild(node);
            }
        }

        node.height = Math.max(getAVLKeyHeight(node.left), getAVLKeyHeight(node.right)) + 1;
        return node;
    }

    //rotate node with left child
    private AVLnode<T> rotateWithLeftChild(AVLnode<T> k2){
        AVLnode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(getAVLKeyHeight(k2.left), getAVLKeyHeight(k2.right)) + 1;
        k1.height = Math.max(getAVLKeyHeight(k1.left), k2.height) + 1;
        return k1;
    }

    //double rotate node with left child
    private AVLnode<T> doubleWithLeftChild(AVLnode<T> k3){
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    //similarly, rotate node with right child
    private AVLnode<T> rotateWithRightChild(AVLnode<T> k1){
        AVLnode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(getAVLKeyHeight(k1.left), getAVLKeyHeight(k1.right)) + 1;
        k2.height = Math.max(getAVLKeyHeight(k2.right), k1.height) + 1;
        return k2;
    }

    //double rotate node with right child
    private AVLnode<T> doubleWithRightChild(AVLnode<T> k3){
        k3.right = rotateWithLeftChild(k3.right);
        return rotateWithRightChild(k3);
    }

    // deletion from a subtree
    private AVLnode<T> delete(T data, AVLnode<T> node){
        if (node == null){
            return node;
        }

        int compareResult = data.compareTo(node.data);
        if (compareResult < 0){
            node.left = delete(data, node.left);
        } else if (compareResult > 0){
            node.right = delete(data, node.right);
        } else if (node.left != null && node.right != null){
            node.data = findMin(node.right).data;
            node.right = delete(node.data, node.right);
        } else {
            node = (node.left != null) ? node.left : node.right;
        }
        return balance(node);
    }

}