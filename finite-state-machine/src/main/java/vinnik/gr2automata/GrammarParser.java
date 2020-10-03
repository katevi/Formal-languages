package vinnik.gr2automata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class GrammarParser {
    public Grammar readGrammarFromFile() {
        Set<Terminal> terminals = new HashSet<>();
        Set<NonTerminal> nonterminals = new HashSet<>();
        Set<Relation> relations = new HashSet<>();

        try {
            Stream<String> strings = Files.lines(Paths.get("grammar.txt"));
        } catch (IOException e) {
            System.out.println("Failed to read grammar from file grammar.txt. Check if it is in working directory.");
        }

        Grammar grammar = new Grammar(relations, nonterminals, terminals);
        return grammar;
    }

    private void parseInput() {

    }
}
