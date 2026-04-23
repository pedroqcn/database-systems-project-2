import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    public static void main(String[] args) throws Exception {
        IndexBuilder ib = new IndexBuilder();
        QueryExecutor qe = new QueryExecutor(ib);
        Scanner scanner = new Scanner(System.in);

        Pattern createPattern = Pattern.compile("CREATE INDEX ON Project2Dataset \\(RandomV\\)");
        Pattern equalityPattern = Pattern.compile("SELECT \\* FROM Project2Dataset WHERE RandomV = (\\d+)");
        Pattern rangePattern = Pattern.compile("SELECT \\* FROM Project2Dataset WHERE RandomV > (\\d+) AND RandomV < (\\d+)");
        Pattern inequalityPattern = Pattern.compile("SELECT \\* FROM Project2Dataset WHERE RandomV != (\\d+)");

        System.out.println("Program is ready and waiting for user command.");
        System.out.println("Commands:");
        System.out.println("CREATE INDEX ON Project2Dataset (RandomV)");
        System.out.println("SELECT * FROM Project2Dataset WHERE RandomV = (value)");
        System.out.println("SELECT * FROM Project2Dataset WHERE RandomV > (value1) AND RandomV < (value2)");
        System.out.println("SELECT * FROM Project2Dataset WHERE RandomV != (value)");

        while (true) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            Matcher m;

            if ((m = createPattern.matcher(line)).matches()) {
                ib.build();
                System.out.println("The hash-based and array-based indexes are built successfully.");
            } else if ((m = equalityPattern.matcher(line)).matches()) {
                int value = Integer.parseInt(m.group(1));
                qe.equalityQuery(value);
            } else if ((m = rangePattern.matcher(line)).matches()) {
                int v1 = Integer.parseInt(m.group(1));
                int v2 = Integer.parseInt(m.group(2));
                qe.rangeQuery(v1, v2);
            } else if ((m = inequalityPattern.matcher(line)).matches()) {
                int value = Integer.parseInt(m.group(1));
                qe.inequalityQuery(value);
            }
            else {
                System.out.println("Invalid command: " + line);
            }

            System.out.println("Program is ready and waiting for user command.");
        }
    }
}
