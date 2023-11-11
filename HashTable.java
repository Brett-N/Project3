/**
 * HashTable class
 * 
 * @author Brettn
 * @author cpiyush854
 * @version 11/1/2023
 *
 * @param <K>
 *            Key
 * @param <V>
 *            Value
 */
public class HashTable<K, V> {

    private Node<K, V>[] buckets;
    private static final int INITIAL_CAPACITY = 10;
    private static final float THRESHOLD = 0.5f;
    private int count;

    /**
     * Node class
     * 
     * @author Brettn
     * @author cpiyush854
     * @version 11/1/2023
     *
     * @param <K>
     *            Key
     * @param <V>
     *            Value
     */
    private static class Node<K, V> {
        private K keyHash;
        private V valueHolder;
        private Node<K, V> followingNode;

        /**
         * Constructor for node class
         * 
         * @param keyHash
         *            the key
         * @param valueHolder
         *            the value
         */
        Node(K keyHash, V valueHolder) {
            this.keyHash = keyHash;
            this.valueHolder = valueHolder;
        }
    }

    /**
     * Default constructor for HashTable class
     */
    @SuppressWarnings("unchecked")
    public HashTable() {
        buckets = new Node[INITIAL_CAPACITY];
    }


    /**
     * Constructor for HashTable class
     * 
     * @param capacity
     *            the size of the hash table
     */
    public HashTable(int capacity) {
        buckets = new Node[capacity];
    }


    /**
     * Adds a node into the table
     * 
     * @param keyHash
     *            key for the node
     * @param valueHolder
     *            value for the node
     */
    public void add(K keyHash, V valueHolder) {
        int bucketIndex = calculateBucketIndex(keyHash);
        Node<K, V> newNode = new Node<>(keyHash, valueHolder);
        insertNode(bucketIndex, newNode);

        if ((float)count / buckets.length >= THRESHOLD) {
            expandMap();
        }
    }


    /**
     * Fetches value that matches key
     * 
     * @param keyHash
     *            the key to match
     * @return value for the matching key
     */
    public V fetch(K keyHash) {
        Node<K, V> node = findNode(keyHash, buckets[calculateBucketIndex(
            keyHash)]);
        return node != null ? node.valueHolder : null;
    }


    /**
     * Deletes node with matching key
     * 
     * @param keyHash
     *            the key to match
     * @return value of deleted node
     */
    public V delete(K keyHash) {
        int bucketIndex = calculateBucketIndex(keyHash);
        Node<K, V> node = buckets[bucketIndex];
        Node<K, V> prev = null;

        while (node != null) {
            if (node.keyHash.equals(keyHash)) {
                if (prev != null) {
                    prev.followingNode = node.followingNode;
                }
                else {
                    buckets[bucketIndex] = node.followingNode;
                }
                count--;
                return node.valueHolder;
            }
            prev = node;
            node = node.followingNode;
        }
        return null;
    }


    /**
     * Inserts node at index
     * 
     * @param index
     *            the spot to insert
     * @param nodeToAdd
     *            the node to insert
     */
    private void insertNode(int index, Node<K, V> nodeToAdd) {
        Node<K, V> existing = buckets[index];
        if (existing == null) {
            buckets[index] = nodeToAdd;
            count++;
        }
        else {
            while (existing.followingNode != null) {
                if (existing.keyHash.equals(nodeToAdd.keyHash)) {
                    existing.valueHolder = nodeToAdd.valueHolder;
                    return;
                }
                existing = existing.followingNode;
            }
            existing.followingNode = nodeToAdd;
            count++;
        }
    }


    /**
     * Expands the HashTable
     */
    @SuppressWarnings("unchecked")
    private void expandMap() {
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];
        count = 0;

        for (Node<K, V> headNode : oldBuckets) {
            while (headNode != null) {
                add(headNode.keyHash, headNode.valueHolder);
                headNode = headNode.followingNode;
            }
        }
    }


    /**
     * Finds a node in the table
     * 
     * @param key
     *            the key to match
     * @param node
     *            the current node
     * @return node that matches key
     */
    private Node<K, V> findNode(K key, Node<K, V> node) {
        while (node != null) {
            if (node.keyHash.equals(key)) {
                return node;
            }
            node = node.followingNode;
        }
        return null;
    }


    /**
     * Calculates the index of a key in the table
     * 
     * @param keyHash
     *            the key to match
     * @return int for index
     */
    private int calculateBucketIndex(K keyHash) {
        return Math.abs(keyHash.hashCode() % buckets.length);
    }


    /**
     * Gets the entry number
     * 
     * @return int for entries
     */
    public int countEntries() {
        return count;
    }
}
