import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainAlphabeticalIndexTest {
    private Map<Integer, String[]> readTextFromFile(String filename) {
        Map<Integer, String[]> map = new TreeMap<>();
        try {
            Stream<String> streamText = Files.lines(Paths.get(filename));
            String text = "";
            int counter = 1;
            for (String i : streamText.collect(Collectors.toList())) {
                i = MainAlphabeticalIndex.cleanFromSeparators(i);
                String[] words = i.split(" ");
                map.put(counter, words);
                counter++;
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void printAlphabeticalIndex1() {
        Map<Integer, String[]> map = readTextFromFile("test1.txt");
        System.out.println(MainAlphabeticalIndex.printAlphabeticalIndex(MainAlphabeticalIndex.generateAlphabeticalIndex(map)));
        MainAlphabeticalIndex.writeToFile(MainAlphabeticalIndex.printAlphabeticalIndex(MainAlphabeticalIndex.generateAlphabeticalIndex(map)));
    }

    @Test
    public void printAlphabeticalIndex2() {
        Map<Integer, String[]> map = readTextFromFile("test2.txt");
        System.out.println(MainAlphabeticalIndex.printAlphabeticalIndex(MainAlphabeticalIndex.generateAlphabeticalIndex(map)));
        MainAlphabeticalIndex.writeToFile(MainAlphabeticalIndex.printAlphabeticalIndex(MainAlphabeticalIndex.generateAlphabeticalIndex(map)));
    }

    @Test
    public void printAlphabeticalIndex3() {
        Map<Integer, String[]> map = readTextFromFile("test3.txt");
        System.out.println(MainAlphabeticalIndex.printAlphabeticalIndex(MainAlphabeticalIndex.generateAlphabeticalIndex(map)));
        MainAlphabeticalIndex.writeToFile(MainAlphabeticalIndex.printAlphabeticalIndex(MainAlphabeticalIndex.generateAlphabeticalIndex(map)));
    }
}
