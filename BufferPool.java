import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The BufferPool class
 *
 * @author Brettn
 * @author cpiyush854
 * @version 11/1/2023
 */
public class BufferPool {
    private static final int BLOCK_SIZE = 4096;
    private HashTable<Integer, DualNode<Buffer>> cacheMap;
    private DoublyLinkedList<Buffer> cacheSequence;
    private RandomAccessFile fileAccessor;
    private int cacheCapacity;
    private int totalBlocks;

    /**
     * Constructor for the BufferPool class
     *
     * @param parameters
     *            the parameters from command line
     * @throws IOException
     *             if error in creating BufferPool
     */
    public BufferPool(String[] parameters) throws IOException {
        fileAccessor = new RandomAccessFile(parameters[0], "rw");
        cacheCapacity = Integer.parseInt(parameters[1]);
        totalBlocks = (int)fileAccessor.length() / BLOCK_SIZE;
        initializeCache();
    }


    /**
     * Creates HashTable for cache
     */
    private void initializeCache() throws IOException {
        cacheMap = new HashTable<>(cacheCapacity);
        cacheSequence = new DoublyLinkedList<>();
        for (int i = 0; i < cacheCapacity; i++) {
            DualNode<Buffer> node = new DualNode<>(new Buffer(fileAccessor,
                -1));
            cacheMap.add(-1, node);
            cacheSequence.prependNode(node);
        }
    }


    /**
     * Accessor method for total blocks
     *
     * @return number of blocks
     */
    public int getTotalBlockCount() {
        return totalBlocks;
    }


    /**
     * Writes data to the disk
     *
     * @param buffer
     *            bytes to write
     * @param size
     *            how many bytes to write
     * @param position
     *            where to write bytes to
     */
    public void writeData(byte[] buffer, int size, int position) {
        try {
            Buffer cacheBlock = seekBlock(position);
            int localOffset = position % BLOCK_SIZE;
            System.arraycopy(buffer, 0, cacheBlock.blockData, localOffset,
                size);
            cacheBlock.setDirty(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Reads data from the disk
     *
     * @param buffer
     *            where to read the bytes to
     * @param size
     *            how many bytes to read
     * @param position
     *            where to read the bytes from
     */
    public void readData(byte[] buffer, int size, int position) {
        try {
            Buffer cacheBlock = seekBlock(position);
            int localOffset = position % BLOCK_SIZE;
            System.arraycopy(cacheBlock.blockData, localOffset, buffer, 0,
                size);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Purges the cache
     *
     * @throws IOException
     *             if error in purge
     */
    public void purge() throws IOException {
        for (Buffer block : cacheSequence) {
            block.sync(); // Only writes back if the block is dirty.
        }
    }


    /**
     * Finds a block in the cache
     *
     * @param position
     *            where to look for the block
     * @return buffer of the block
     * @throws IOException
     *             if error in seeking block
     */
    private Buffer seekBlock(int position) throws IOException {
        int blockIdx = position / BLOCK_SIZE;
        DualNode<Buffer> node = cacheMap.fetch(blockIdx);
        if (node != null) {
            Quicksort.incrementSuccessfulRetrievals();
            cacheSequence.elevate(node);
        }
        else {
            Quicksort.incrementRetrievalAttempts();
            node = fetchLastAndResync(blockIdx);
        }
        return node.getElementData();
    }


    /**
     * Handles cache block synchronization and updating
     * 
     * @param blockIdx
     *            the index of the block
     * @return node the buffer is at
     * @throws IOException
     *             if error in fetching and syncing
     */
    private DualNode<Buffer> fetchLastAndResync(int blockIdx)
        throws IOException {
        DualNode<Buffer> node = cacheSequence.fetchTail();
        cacheMap.delete(node.getElementData().blockIndex);
        node.getElementData().sync();
        node.getElementData().updateBlockIndexAndPosition(blockIdx);
        node.getElementData().load();
        cacheMap.add(blockIdx, node);
        cacheSequence.elevate(node);
        return node;
    }

    /**
     * Class for a Buffer object
     * 
     * @author Brettn
     * @author cpiyush854
     * @version 11/1/2023
     *
     */
    private class Buffer {
        private int blockIndex;
        private byte[] blockData;
        private RandomAccessFile fileStream;
        private int filePosition;
        private boolean dirty;

        /**
         * Constructor for the Buffer class
         * 
         * @param fileStream
         *            the disk file
         * @param blockIndex
         *            index within the pool
         */
        public Buffer(RandomAccessFile fileStream, int blockIndex) {
            this.blockIndex = blockIndex;
            this.fileStream = fileStream;
            this.filePosition = blockIndex * BLOCK_SIZE;
            blockData = new byte[BLOCK_SIZE];
            this.dirty = false;
        }


        /**
         * Sets the block to dirty
         * 
         * @param dirty
         */
        public void setDirty(boolean dirty) {
            this.dirty = dirty;
        }


        /**
         * Syncs the data in the buffer
         * 
         * @throws IOException
         *             if error in syncing data
         */
        public void sync() throws IOException {
            if (dirty) {
                fileStream.seek(filePosition);
                fileStream.write(blockData);
                dirty = false;
                Quicksort.incrementStorageOperations();
            }
        }


        /**
         * Loads the buffer data
         * 
         * @throws IOException
         *             if error in loading the data
         */
        public void load() throws IOException {
            fileStream.seek(filePosition);
            fileStream.read(blockData);
        }


        /**
         * Updates the block index and file position
         * 
         * @param blockIdx
         *            new block index
         */
        private void updateBlockIndexAndPosition(int blockIdx) {
            this.blockIndex = blockIdx;
            this.filePosition = blockIdx * BLOCK_SIZE;
        }
    }
}
