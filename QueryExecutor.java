import java.util.*;
import java.nio.file.*;
import java.io.RandomAccessFile;

public class QueryExecutor {
    private IndexBuilder indexes;

    public QueryExecutor(IndexBuilder indexes) {
        this.indexes = indexes;
    }

    public void equalityQuery(int value) throws Exception {
        // Capture the start timne so we can report elapsed time later
        long startTime = System.currentTimeMillis();

        // Get the hash index from the IndexBuilder
        HashMap<Integer, List<int[]>> hashIndex = indexes.getHashIndex();

        // If the hash index is empty, no CREATE INDEX has run yet
        // Fall back to a full table scan
        if (hashIndex.isEmpty()) {
            fullScanEquality(value, startTime);
            return;
        }

        // Index is available - lookup the value in the hash index
        List<int[]> locations = hashIndex.get(value);

        // If the key isn't in the map, no records match
        if (locations == null) {
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.println("No records found.");
            System.out.println("Elapsed time: " + elapsed + "ms");
            return;
        }

        // Track which files we've read to report the count
        Set<Integer> filesRead = new HashSet<>();

        // For each location, open the file, seek to the offset, read 40 bytes
        for (int[] location : locations) {
            int fileId = location[0];
            int offset = location[1];

            String path = "Project2Dataset/F" + fileId + ".txt";
            RandomAccessFile raf = new RandomAccessFile(path, "r");

            raf.seek(offset);
            byte[] buf = new byte[40];
            raf.read(buf);
            raf.close();

            // Convert the 40 bytes into a printable String and print the record
            System.out.println(new String(buf));

            // Record that this file was touched
            filesRead.add(fileId);
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Found " + locations.size() + " records.");
        System.out.println("Elapsed time: " + elapsed + "ms");
        System.out.println("Files read: " + filesRead.size());
    }

    public void rangeQuery(int v1, int v2) throws Exception {
        // Capture start time for elapsed-time reporting
        long startTime = System.currentTimeMillis();

        // Get the array index from the IndexBuilder
        List<int[]>[] arrayIndex = indexes.getArrayIndex();

        // Detect whether the array index has been built.
        if (indexes.getHashIndex().isEmpty()) {
            fullScanRange(v1, v2, startTime);
            return;
        }

        // Track which distinct file we've opened
        Set<Integer> filesRead = new HashSet<>();

        // For each value in the range, loop through the array index
        for (int i = v1; i <= v2; i++) {
            if (arrayIndex[i] == null) {
                continue;
            }

            // For each location at this slot, open the file, seek, read, print
            for (int[] location : arrayIndex[i]) {
                int fileId = location[0];
                int offset = location[1];

                String path = "Project2Dataset/F" + fileId + ".txt";
                RandomAccessFile raf = new RandomAccessFile(path, "r");

                raf.seek(offset);
                byte[] buf = new byte[40];
                raf.read(buf);
                raf.close();

                System.out.println(new String(buf));
                filesRead.add(fileId);
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Index used: Array");
        System.out.println("Found " + (v2 - v1 + 1) + " records.");
        System.out.println("Elapsed time: " + elapsed + "ms");
        System.out.println("Files read: " + filesRead.size());
    }

    private void fullScanEquality(int value, long startTime) throws Exception {
        int filesRead = 0;

        // Loop through every file in the dataset
        for (int fileId = 1; fileId <= 99; fileId++){
            String path = "Project2Dataset/F" + fileId + ".txt";
            byte[] data = Files.readAllBytes(Paths.get(path));

            // Loop through every record in the file
            for (int j = 0; j < 100; j++) {
                int offset = j * 40;

                // Extract the value from the record
                String randomV = new String(data, offset + 33, 4);
                int recordValue = Integer.parseInt(randomV);

                // If the value matches, print the record
                if (recordValue == value) {
                    System.out.println(new String(data, offset, 40));
                    filesRead++;
                }
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Index used: Table Scan");
        System.out.println("Found " + filesRead + " records.");
        System.out.println("Elapsed time: " + elapsed + "ms");
    }

    private void fullScanRange(int v1, int v2, long startTime) throws Exception {
        int filesRead = 0;

        // Loop through every file in the dataset
        for (int fileId = 1; fileId <= 99; fileId++){
            String path = "Project2Dataset/F" + fileId + ".txt";
            byte[] data = Files.readAllBytes(Paths.get(path));
            filesRead++;

            for (int j = 0; j < 100; j++) {
                int offset = j * 40;
                String randomV = new String(data, offset + 33, 4);
                int recordValue = Integer.parseInt(randomV);

                if (recordValue > v1 && recordValue < v2) {
                    System.out.println(new String(data, offset, 40));
                }
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Index used: Table Scan");
        System.out.println("Found " + filesRead + " records.");
        System.out.println("Elapsed time: " + elapsed + "ms");
    }
}
