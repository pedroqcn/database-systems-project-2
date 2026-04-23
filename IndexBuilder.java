import java.util.*;
import java.nio.file.*;

public class IndexBuilder {
    private HashMap<Integer, List<int[]>> hashIndex;
    private List<int[]>[] arrayIndex;

    public IndexBuilder() {
        hashIndex = new HashMap<>();
        arrayIndex = new List[5001];
    }

    public void build() throws Exception {
        for (int fileId = 1; fileId <= 99; fileId++){
            String path = "Project2Dataset/F" + fileId + ".txt";
            byte[] data = Files.readAllBytes(Paths.get(path));

            for (int j = 0; j < 100; j++) {
                int offset = j * 40;
                String randomV = new String(data, offset + 33, 4);
                int value = Integer.parseInt(randomV);

                hashIndex.computeIfAbsent(value, k -> new ArrayList<>()).add(new int[]{fileId, offset});

                if (arrayIndex[value] == null) {
                    arrayIndex[value] = new ArrayList<>();
                }
                arrayIndex[value].add(new int[]{fileId, offset});
            }
        }
    }

    public HashMap<Integer, List<int[]>> getHashIndex() {
        return hashIndex;
    }

    public List<int[]>[] getArrayIndex() {
        return arrayIndex;
    }

    public static void main(String[] args) throws Exception {
        IndexBuilder ib = new IndexBuilder();
        ib.build();

        System.out.println("Hash Index has " + ib.hashIndex.size() + " distinct keys");

        int total = 0;
        for (List<int[]> locations : ib.hashIndex.values()) {
            total += locations.size();
        }
        System.out.println("Hash Index has " + total + " locations");

        System.out.println("arrayIndex[4037] size: " + (ib.arrayIndex[4037] == null ? 0 : ib.arrayIndex[4037].size()));
    }
}