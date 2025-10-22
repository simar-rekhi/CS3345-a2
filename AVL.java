// AVL.java acts as a custom implementation of the AVL Tree Data Structure 


public class AVL<T extends Comparable<T>> {

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

        // attributes of the AVL tree node
        T data;
        AVLnode<T> left;
        AVLnode<T> right;
        int height;
    }
    
    private AVLnode<T> root;

    public AVL(){
        root = null;
    }

    // helper to compute node height (null => -1)
    private int height(AVLnode<T> node){
        return (node == null) ? -1 : node.height;
    }

    // method to get the height of the tree
    public int height(){
        return height(root);
    }

    // inserting into a subtree
    private AVLnode<T> insert(T data, AVLnode<T> node){
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

    // find minimum in subtree
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

    private AVLnode<T> balance(AVLnode<T> node){
        if (node == null){
            return node;
        }

        if (height(node.left) - height(node.right) > BALANCE_FACTOR){
            if (height(node.left.left) >= height(node.left.right)){
                node = rotateWithLeftChild(node);
            } else {
                node = doubleWithLeftChild(node);
            }
        } else if (height(node.right) - height(node.left) > BALANCE_FACTOR){
            if (height(node.right.right) >= height(node.right.left)){
                node = rotateWithRightChild(node);
            } else {
                node = doubleWithRightChild(node);
            }
        }

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        return node;
    }

    //rotate node with left child
    private AVLnode<T> rotateWithLeftChild(AVLnode<T> k2){
        AVLnode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
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
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
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