package vinnik.firstfollow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        checkWordsFromFile();
    }

    private static void checkWordsFromFile() {
        List<String> words = new ArrayList<>();
        try {
            List<String> wordsFromFile = Files.lines(Paths.get("words.txt")).collect(Collectors.toList());
            words.addAll(wordsFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GrammarParser parser = new GrammarParser();
        Grammar grammar = parser.readGrammarFromFile();

        grammar.calculateFirsts(3);
        grammar.calculateFollows(3);
    }
}