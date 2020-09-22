import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final Map<Integer, String[]> text = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to alphabetical index! Please, enter text, press 0 to end:\n");

        String inputValue = "";
        inputValue = scanner.nextLine();

        int counter = 1;
        while (!inputValue.equals("0")) {
            inputValue = cleanFromSeparators(inputValue);

            String[] wordsInString = inputValue.split(" ");
            text.put(counter, wordsInString);
            inputValue = scanner.nextLine();
            counter++;
        }
        String indexPrint = printAlphabeticalIndex(generateAlphabeticalIndex(text));
        System.out.println("Alphabetic index built. Check alphabetical-index.txt file.\n");
        writeToFile(indexPrint);
    }

    public static Multimap<String, Integer> generateAlphabeticalIndex(Map<Integer, String[]> text) {
        Multimap<String, Integer> index = TreeMultimap.create();
        for (Map.Entry<Integer, String[]> entry : text.entrySet()) {
            for (String word : entry.getValue()) {
                if (!index.get(word).contains(entry.getKey())) {
                    index.put(word.toLowerCase(), entry.getKey());
                }
            }
        }
        return index;
    }

    private static void writeToFile(String indexPrint) {
        try {
            FileWriter writer = new FileWriter("alphabetical-index.txt");
            writer.write(indexPrint);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String cleanFromSeparators(String string) {
        string = string.replace(",", "");
        string = string.replace(".", "");
        string = string.replace(";", "");
        string = string.replace("?", "");
        return string.replace("!", "");
    }

    public static String printAlphabeticalIndex(Multimap<String, Integer> alphabeticalIndex) {
        StringBuilder print = new StringBuilder();
        alphabeticalIndex
                .asMap()
                .forEach((key, value) -> {
                    print.append(key);
                    print.append(" ");
                    value.forEach(t -> {
                        print.append(t);
                        print.append(" ");
                    });
                    print.append("\n");
                });
        return print.toString();
    }
}
