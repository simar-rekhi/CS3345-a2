public class Hash_Table {
    private int table_size;
    private Node[] chainTable; // used for chaining
    private int[] quadTable; // used for quadratic probing
    private boolean[] occupied; // tracks vacancy of cells needed for quadratic probing

    // Node class for chaining
    private static class Node {
        int key;
        Node next;

        Node(int key) {
            this.key = key;
            this.next = null;
        }
    }

    // constructor
    public Hash_Table(int size) {
        this.table_size = size;
        chainTable = new Node[size];
        quadTable = new int[size];
        occupied = new boolean[size];
        for (int i = 0; i < size; i++) {
            quadTable[i] = -1; // Empty slot
        }
    }

    // function type 1: Hash Function using Modulo Division
    private int hash(int key) {
        return key % table_size;
    }

    // methods to insert
    // insertion via chaining
    public void insertChain(int key) {
        int index = hash(key);
        Node newNode = new Node(key);

        if (chainTable[index] == null) {
            chainTable[index] = newNode;
        } else {
            Node current = chainTable[index];
            while (current.next != null) {
                if (current.key == key)
                    return; // avoid duplicates
                current = current.next;
            }
            if (current.key != key)
                current.next = newNode;
        }
    }

    // insertion via quadratic probing
    public void insertQuadratic(int key) {
        int hashVal = hash(key);
        int i = 0;
        int index;

        while (i < table_size) {
            index = (hashVal + i * i) % table_size;
            if (!occupied[index]) {
                quadTable[index] = key;
                occupied[index] = true;
                return;
            }
            i++;
        }
        System.out.println("Quadratic probing table full! Cannot insert key: " + key);
    }

    // methods to lookup
    // lookup via chaining
    public boolean lookupChain(int key) {
        int index = hash(key);
        Node current = chainTable[index];
        while (current != null) {
            if (current.key == key)
                return true;
            current = current.next;
        }
        return false;
    }

    // lookup using quadratic probing
    public boolean lookupQuadratic(int key) {
        int hashVal = hash(key);
        int i = 0;
        int index;

        while (i < table_size) {
            index = (hashVal + i * i) % table_size;
            if (!occupied[index])
                return false; // empty means not found
            if (quadTable[index] == key)
                return true;
            i++;
        }
        return false;
    }

    // some other helper methods
    // prints all the keys in chaining bucket at a specific index
    public void getChain(int index) {
        if (index < 0 || index >= table_size) {
            System.out.println("Invalid index.");
            return;
        }
        Node current = chainTable[index];
        if (current == null) {
            System.out.println("Bucket " + index + " is empty.");
            return;
        }
        System.out.print("Bucket " + index + ": ");
        while (current != null) {
            System.out.print(current.key + " ");
            current = current.next;
        }
        System.out.println();
    }

    // prints the index at which the key is stored (part of quadratic probing)
    public void getQuadraticIndex(int key) {
        int hashVal = hash(key);
        int i = 0;
        int index;

        while (i < table_size) {
            index = (hashVal + i * i) % table_size;
            if (occupied[index] && quadTable[index] == key) {
                System.out.println("Key " + key + " found at index " + index);
                return;
            }
            i++;
        }
        System.out.println("Key " + key + " not found in quadratic probing table.");
    }

    // print all quadratic probing entries
    public void printQuadraticTable() {
        System.out.println("Quadratic Probing Table:");
        for (int i = 0; i < table_size; i++) {
            System.out.println(i + " : " + (occupied[i] ? quadTable[i] : "empty"));
        }
    }

    // print all chaining entries
    public void printChainingTable() {
        System.out.println("Chaining Table:");
        for (int i = 0; i < table_size; i++) {
            System.out.print(i + ": ");
            Node current = chainTable[i];
            while (current != null) {
                System.out.print(current.key + " -> ");
                current = current.next;
            }
            System.out.println("null");
        }
    }

}