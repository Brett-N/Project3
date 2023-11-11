import java.io.IOException;
import java.util.Random;

/**
 * The Sort class
 * 
 * @author Brettn
 * @author cpiyush854
 * @version 11/1/2023
 *
 */
public class Sort {
    private BufferPool bufferManager;
    private int storageSize;
    private static final int SIZE_OF_BLOCK = 4096;
    private static final int SIZE_OF_DATA = 4;
    private static final int SORT_THRESHOLD = 20;
    private static Random rng = new Random();

    /**
     * Constructor for sort class
     * 
     * @param parameters
     *            for sort
     * @throws IOException
     *             if error in sort
     */
    public Sort(String[] parameters) throws IOException {
        bufferManager = new BufferPool(parameters);
        this.storageSize = bufferManager.getTotalBlockCount() * SIZE_OF_BLOCK
            / SIZE_OF_DATA;
    }


    /**
     * Makes the call to quicksort
     */
    public void sortFile() {
        performQuickSort(0, storageSize - 1);
    }


    /**
     * Does the main quicksort behavior
     * 
     * @param start
     *            the index to start the sort
     * @param end
     *            the index to end the sort
     */
    private void performQuickSort(int start, int end) {
        if (checkUniformity(start, end)) {
            return;
        }
        if (end - start < SORT_THRESHOLD) {
            performInsertionSort(start, end);
        }
        else {
            if (start < end) {
                executeSwap(end, selectRandomPivot(start, end));
                int pivotLoc = executePartition(start, end);
                performQuickSort(start, pivotLoc - 1);
                performQuickSort(pivotLoc + 1, end);
            }
        }
    }


    /**
     * Partitions the sub array
     * 
     * @param start
     *            the index to start
     * @param end
     *            the index to end
     * @return int for partition result
     */
    private int executePartition(int start, int end) {
        byte[] pivotValue = fetchRecord(end);
        int i = start - 1;
        for (int j = start; j <= end - 1; j++) {
            byte[] current = fetchRecord(j);
            if (compareRecords(current, pivotValue) <= 0) {
                i++;
                if (i != j) {
                    byte[] iRecord = fetchRecord(i);
                    executeSwap(i, j, iRecord, current);
                }
            }
        }
        byte[] recordAfterI = fetchRecord(i + 1);
        executeSwap(i + 1, end, recordAfterI, pivotValue);
        return i + 1;
    }


    /**
     * Does the insertion sort to reduce time
     * 
     * @param start
     *            the index to start
     * @param end
     *            the index to end
     */
    private void performInsertionSort(int start, int end) {
        for (int i = start + 1; i <= end; i++) {
            int j = i - 1;
            byte[] recordToInsert = fetchRecord(i);
            byte[] comparisonRecord = fetchRecord(j);
            while (j >= start && compareRecords(comparisonRecord,
                recordToInsert) > 0) {
                executeSwap(j, j + 1, comparisonRecord, recordToInsert);
                j--;
                if (j >= 0)
                    comparisonRecord = fetchRecord(j);
            }
        }
    }


    /**
     * Checks for uniformity
     * 
     * @param start
     *            the index to start
     * @param end
     *            the index to end
     * @return true if uniform, false if not
     */
    private boolean checkUniformity(int start, int end) {
        byte[] first = fetchRecord(start);
        for (int i = start + 1; i <= end; i++) {
            byte[] current = fetchRecord(i);
            if (compareRecords(first, current) != 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * Finds pivot for quicksort
     * 
     * @param start
     *            the index to start
     * @param end
     *            the index to end
     * @return the pivot index
     */
    private int selectRandomPivot(int start, int end) {
        return rng.nextInt(end - start + 1) + start;
    }


    /**
     * Fetches records at an index
     * 
     * @param idx
     *            the index to fetch from
     * @return byte array of data
     */
    private byte[] fetchRecord(int idx) {
        byte[] dataBytes = new byte[SIZE_OF_DATA];
        bufferManager.readData(dataBytes, SIZE_OF_DATA, idx * SIZE_OF_DATA);
        return dataBytes;
    }


    /**
     * Compares two arrays
     * 
     * @param firstRecord
     *            first array to compare
     * @param secondRecord
     *            second array to compare
     * @return int signaling comparison results
     */
    private int compareRecords(byte[] firstRecord, byte[] secondRecord) {
        int num1 = (firstRecord[0] << 8) | (firstRecord[1] & 0xFF);
        int num2 = (secondRecord[0] << 8) | (secondRecord[1] & 0xFF);
        return Integer.compare(num1, num2);
    }


    /**
     * Most efficient swap
     * 
     * @param idxOne
     *            index of first records
     * @param idxTwo
     *            index of second records
     * @param firstRecord
     *            first data set
     * @param secondRecord
     *            second data set
     */
    private void executeSwap(
        int idxOne,
        int idxTwo,
        byte[] firstRecord,
        byte[] secondRecord) {
        bufferManager.writeData(secondRecord, SIZE_OF_DATA, idxOne
            * SIZE_OF_DATA);
        bufferManager.writeData(firstRecord, SIZE_OF_DATA, idxTwo
            * SIZE_OF_DATA);
    }


    /**
     * Less efficient swap
     * 
     * @param idxOne
     *            the index for the first set of records
     * @param idxTwo
     *            the index for the second set of records
     */
    private void executeSwap(int idxOne, int idxTwo) {
        byte[] recordOne = fetchRecord(idxOne);
        byte[] recordTwo = fetchRecord(idxTwo);
        bufferManager.writeData(recordTwo, SIZE_OF_DATA, idxOne * SIZE_OF_DATA);
        bufferManager.writeData(recordOne, SIZE_OF_DATA, idxTwo * SIZE_OF_DATA);
    }


    /**
     * Cleans the BufferPool
     * 
     * @throws IOException
     *             if error in cleaning the BufferPool
     */
    public void cleanUp() throws IOException {
        bufferManager.purge();
    }
}
