import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {
    public static void readFile(int fileId) throws Exception {
        String path = "Project2Dataset/F" + fileId + ".txt";
        byte[] data = Files.readAllBytes(Paths.get(path));

        for (int j = 0; j < 100; j++) {
            int offset = j * 40;
            String randomV = new String(data, offset + 33, 4);
            int value = Integer.parseInt(randomV);
            System.out.println("Record " + j + ": RandomV=" + value + ", offset=" + offset);
        }
    }

    public static void main(String[] args) throws Exception {
        readFile(1);
    }

    
}