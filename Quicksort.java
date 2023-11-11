import java.io.FileWriter;
import java.io.IOException;

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

/**
 * The class containing the main method.
 *
 * @author Brettn
 * @author cpiyush854
 * @version 11/1/2023
 */
public class Quicksort {

    // Tracking attributes
    private static String identifier;
    private static int hits = 0;
    private static int attempts = 0;
    private static int writes = 0;
    private static long duration = 0;

    /**
     * Main method for the class
     * 
     * @param args
     *            command line agruments
     * @throws IOException
     *             when error in main method
     */
    public static void main(String[] args) throws IOException {
        setupIdentifier(args[0]);
        long startTime = System.nanoTime();

        Sort algorithm = new Sort(args);
        algorithm.sortFile();
        algorithm.cleanUp();

        computeDuration(System.nanoTime() - startTime);

        logMetrics(args[2]);
    }


    /**
     * Writes the metrics for the program
     * 
     * @param statsFile
     *            the file to write the stats to
     * @throws IOException
     *             if not able to generate report
     */
    private static void logMetrics(String statsFile) throws IOException {
        try (FileWriter writer = new FileWriter(statsFile, true)) {
            writer.write(generateReport());
        }
    }


    /**
     * Writes the string representation of all the stats
     * 
     * @return a string of the statistics
     */
    private static String generateReport() {
        return String.format(
            "\nFile: %s\nHits: %d\nReads: %d\nWrites: %d\nTime: %d\n",
            identifier, hits, attempts, writes, duration / 1000000);
    }


    /**
     * Gets the identifier
     * 
     * @return string of identifier
     */
    public static String getResourceIdentifier() {
        return identifier;
    }


    /**
     * Sets the identifier
     * 
     * @param name
     *            the String to set the identifier too
     */
    private static void setupIdentifier(String name) {
        identifier = name;
    }


    /**
     * Gets the successful hits
     * 
     * @return number of successful hits
     */
    public static int getSuccessfulRetrievals() {
        return hits;
    }


    /**
     * Increments successful hits
     */
    public static void incrementSuccessfulRetrievals() {
        hits++;
    }


    /**
     * Sets value for hits
     * 
     * @param newHits
     *            the new value of hits
     */
    public static void updateSuccessfulRetrievals(int newHits) {
        hits = newHits;
    }


    /**
     * Gets value for attempts
     * 
     * @return retrieval attempts
     */
    public static int getRetrievalAttempts() {
        return attempts;
    }


    /**
     * Increments attempted hits
     */
    public static void incrementRetrievalAttempts() {
        attempts++;
    }


    /**
     * Sets value for hits
     * 
     * @param newReads
     *            the new value of attempts
     */
    public static void updateRetrievalAttempts(int newReads) {
        attempts = newReads;
    }


    /**
     * Gets value for writes
     * 
     * @return the value for writes
     */
    public static int getStorageOperations() {
        return writes;
    }


    /**
     * Increments writes value
     */
    public static void incrementStorageOperations() {
        writes++;
    }


    /**
     * Sets write value
     * 
     * @param newWrites
     *            the new value for writes
     */
    public static void updateStorageOperations(int newWrites) {
        writes = newWrites;
    }


    /**
     * Gets value for duration
     * 
     * @return duration
     */
    public static long getOperationDuration() {
        return duration;
    }


    /**
     * Sets value for duration
     * 
     * @param newDuration
     *            the value of duration to be set to
     */
    private static void computeDuration(long newDuration) {
        duration = newDuration;
    }
}
