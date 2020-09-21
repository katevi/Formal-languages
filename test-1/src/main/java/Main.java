import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Multimap<String, Integer> myAlphabeticalIndex = TreeMultimap.create();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to alphabetical index! Please, enter text, press 0 to end:\n");

        List<String> text = new LinkedList<>();

        String inputValue = "";
        inputValue = scanner.nextLine();

        int counter = 1;
        while (!inputValue.equals("0")) {
            inputValue = cleanFromSeparators(inputValue);

            String[] wordsInString = inputValue.split(" ");
            for (String word : wordsInString) {
                if (!myAlphabeticalIndex.get(word).contains(counter)) {
                    myAlphabeticalIndex.put(word.toLowerCase(), counter);
                }
            }
            text.add(inputValue);
            inputValue = scanner.nextLine();
            counter++;
        }
        printToFile(myAlphabeticalIndex);
        System.out.println("Alphabetic index built. Check alphabetical-index.txt file.\n");
    }

    private static String cleanFromSeparators(String string) {
        string = string.replace(",", "");
        string = string.replace(".", "");
        string = string.replace(";", "");
        string = string.replace("?", "");
        return string.replace("!", "");
    }

    private static void printToFile(Multimap<String, Integer> alphabeticalIndex) {
        try {
            FileWriter fileWriter = new FileWriter("alphabetical-index.txt");
            alphabeticalIndex
                    .asMap()
                    .forEach((key, value) -> {
                        //System.out.print(key + " ");
                        try {
                            fileWriter.write(key + " ");
                            value.forEach(t -> {
                                try {
                                    fileWriter.write(t + " ");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            fileWriter.write("\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
