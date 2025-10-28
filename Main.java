import java.io.*;
import java.util.*;

public class Main {
    private static final int[] N = { 1000, 10000, 100000 };
    private static final int[] CHAIN_SIZE = { 928, 8329, 83329 };
    private static final int[] QUAD_SIZE = { 2003, 20011, 200003 };

    public static void main(String[] args) {
        System.out.println("Data Structure Performance Comparing Log");

        String basePath = "C:/DevTools/Projects/CS3345-a2/";

        String[] insertFiles = {
                basePath + "iter1_insert_keys.txt",
                basePath + "iter2_insert_keys.txt",
                basePath + "iter3_insert_keys.txt"
        };

        String[] searchFiles = {
                basePath + "iter1_search_keys.txt",
                basePath + "iter2_search_keys.txt",
                basePath + "iter3_search_keys.txt"
        };

        // this warms up JVM
        warmUpJVM();

        for (int i = 0; i < 3; i++) {
            System.out.println("\nDataset: " + (i + 1) + " (" + N[i] + " elements)");
            List<Integer> insertKeys, searchKeys;
            try {
                insertKeys = readKeys(insertFiles[i]);
                searchKeys = readKeys(searchFiles[i]);
            } catch (IOException e) {
                System.err.println("Error reading files: " + e.getMessage());
                continue;
            }

            // AVL tree
            System.out.println("\n  AVL Tree");
            try {
                testAVL(insertKeys, searchKeys);
            } catch (Exception e) {
                System.err.println("AVL Error: " + e.getMessage());
            }

            // Splay Tree
            System.out.println("\n  Splay Tree");
            try {
                testSplay(insertKeys, searchKeys);
            } catch (Exception e) {
                System.err.println("Splay Error: " + e.getMessage());
            }

            // Hash Table - chaining
            System.out.println("\n  Hash Table (chaining)");
            try {
                testHash_Chaining(insertKeys, searchKeys, CHAIN_SIZE[i]);
            } catch (Exception e) {
                System.err.println("Hash (Chaining) Error: " + e.getMessage());
            }

            // Hash Table - quadratic probing
            System.out.println("\n  Hash Table (quadratic probing)");
            try {
                testHash_Probing(insertKeys, searchKeys, QUAD_SIZE[i]);
            } catch (Exception e) {
                System.err.println("Hash (Quadratic) Error: " + e.getMessage());
            }
        }
    }

    // JVM warmup method
    private static void warmUpJVM() {
        AVL<Integer> temp = new AVL<>();
        for (int i = 0; i < 1000; i++)
            temp.insert(i);
        System.out.println("warmup complete, you may proceed");
    }

    // reading files
    private static List<Integer> readKeys(String filename) throws IOException {
        List<Integer> keys = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty())
                    keys.add(Integer.parseInt(line));
            }
        }
        return keys;
    }

    // measurement method
    private static long[] measure(Runnable operation) {
        try {
            System.gc();
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }

        Runtime rt = Runtime.getRuntime();
        long before = rt.totalMemory() - rt.freeMemory();
        long start = System.nanoTime();

        operation.run();

        long end = System.nanoTime();
        long after = rt.totalMemory() - rt.freeMemory();
        long elapsed = (end - start) / 1_000_000; // in ms
        long used = after - before;
        return new long[] { elapsed, used };
    }

    // AVL Testing
    private static void testAVL(List<Integer> insertKeys, List<Integer> searchKeys) {
        AVL<Integer> avl = new AVL<>();

        long[] insertStats = measure(() -> {
            for (Integer key : insertKeys)
                avl.insert(key);
        });
        System.out.printf("Insert: Time: %d ms | Memory: %d bytes%n", insertStats[0], insertStats[1]);

        long[] searchStats = measure(() -> {
            for (Integer key : searchKeys)
                avl.lookup(key);
        });
        System.out.printf("Search: Time: %d ms | Memory: %d bytes%n", searchStats[0], searchStats[1]);
    }

    // Splay Testing
    private static void testSplay(List<Integer> insertKeys, List<Integer> searchKeys) {
        Splay<Integer> splay = new Splay<>();

        long[] insertStats = measure(() -> {
            for (Integer key : insertKeys) {
                try {
                    var insertMethod = Splay.class.getDeclaredMethod("insert", Object.class,
                            Class.forName("Splay$SplayNode"));
                    insertMethod.setAccessible(true);
                    insertMethod.invoke(splay, key, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.printf("Insert: Time: %d ms | Memory: %d bytes%n", insertStats[0], insertStats[1]);

        long[] searchStats = measure(() -> {
            try {
                var splayMethod = Splay.class.getDeclaredMethod("splay", Object.class,
                        Class.forName("Splay$SplayNode"));
                splayMethod.setAccessible(true);
                for (Integer key : searchKeys) {
                    splayMethod.invoke(splay, key, null);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        System.out.printf("Search: Time: %d ms | Memory: %d bytes%n", searchStats[0], searchStats[1]);
    }

    // testing Hash - Chaining
    private static void testHash_Chaining(List<Integer> insertKeys, List<Integer> searchKeys, int size) {
        Hash_Table ht = new Hash_Table(size);

        long[] insertStats = measure(() -> {
            for (Integer key : insertKeys)
                ht.insertChain(key);
        });
        System.out.printf("Insert: Time: %d ms | Memory: %d bytes%n", insertStats[0], insertStats[1]);

        long[] searchStats = measure(() -> {
            for (Integer key : searchKeys)
                ht.lookupChain(key);
        });
        System.out.printf("Search: Time: %d ms | Memory: %d bytes%n", searchStats[0], searchStats[1]);
    }

    // testing Hashing - quadratic probing
    private static void testHash_Probing(List<Integer> insertKeys, List<Integer> searchKeys, int size) {
        Hash_Table ht = new Hash_Table(size);

        long[] insertStats = measure(() -> {
            for (Integer key : insertKeys)
                ht.insertQuadratic(key);
        });
        System.out.printf("Insert: Time: %d ms | Memory: %d bytes%n", insertStats[0], insertStats[1]);

        long[] searchStats = measure(() -> {
            for (Integer key : searchKeys)
                ht.lookupQuadratic(key);
        });
        System.out.printf("Search: Time: %d ms | Memory: %d bytes%n", searchStats[0], searchStats[1]);
    }
}
