import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableReader {
    public String[][] readTable(String filename) {
        List<String> strings = new ArrayList<>();
        try {
             strings = Files.lines(Paths.get(filename)).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Check that file \" + filename + \".txt\" is in work directory.");
        }
        if (strings.isEmpty()) {
            System.out.println("Invalid format of file. Check that file \"" + filename + ".txt\" is in work directory and has correct format.");
        } else {
            System.out.println("File parsed successfully.");
            return parseInputFromFile(strings);
        }
        return null;
    }

    private String[][] parseInputFromFile(List<String> strings) {
        String inputTitle = strings.get(0);
        String[] inputSymbols = inputTitle.split(" ");

        String[][] tableOfTransitions = new String[inputSymbols.length][strings.size()];

        for (int i = 0; i < tableOfTransitions.length; i++) {
            tableOfTransitions[i][0] = inputSymbols[i];
        }

        int counter = 0;
        for (String string : strings) {
            for (int i = 0; i < tableOfTransitions.length; i++) {
                String[] cells = string.split(" ");
                tableOfTransitions[i][counter] = cells[i];
            }
            counter++;
        }
        return tableOfTransitions;
    }
}
