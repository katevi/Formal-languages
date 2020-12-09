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
        //List<List<String>> rightPart = grammar.getRelations().stream().map(t
                //-> t.getRightPart().stream().map(s -> s.toString()).collect(Collectors.toList())).collect(Collectors.toList());
        grammar.getRelations().forEach(t ->
                System.out.println(t.getRelationPrint()));

        grammar.getNonterminals().forEach(t -> System.out.print(t + " "));
        grammar.getTerminals().forEach(t -> System.out.print(t + " "));
        System.out.println(grammar.getStartNonterminal());
    }

    private static void printTable(String[][] tableOfTransitions) {
        int columnWidth = 37;
        System.out.println("Filling table...");
        for (int i = 0; i < tableOfTransitions.length; i++) {
            for (int j = 0; j < (tableOfTransitions[0].length) * columnWidth; j++) {
                System.out.print("_");
            }
            System.out.println();
            for (int j = 0; j < tableOfTransitions[0].length; j++) {
                System.out.format("%35s |", tableOfTransitions[i][j]);
            }
            System.out.println();
        }

        for (int j = 0; j < (tableOfTransitions[0].length) * columnWidth; j++) {
            System.out.print("_");
        }
        System.out.println();
    }
}