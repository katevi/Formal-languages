package vinnik.gr2automata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrammarParser {
    public Grammar readGrammarFromFile() {
        List<String> grammarStrings = new ArrayList<>();
        try {
             grammarStrings.addAll(Files.lines(Paths.get("grammar.txt")).collect(Collectors.toList()));
        } catch (IOException e) {
            System.out.println("Failed to read grammar from file grammar.txt. Check if it is in working directory.");
        }

        if (grammarStrings.size() > 0) {
            return parseInput(grammarStrings);
        } else {
            System.out.println("Failed to read grammar from file. Check if file grammar.txt is empty.");
            return null;
        }
    }

    private Grammar parseInput(List<String> grammarStrings) {
        Set<Terminal> terminals = new LinkedHashSet<>();
        Set<NonTerminal> nonterminals = new LinkedHashSet<>();
        Set<Relation> relations = new LinkedHashSet<>();
        for (String string : grammarStrings) {
            int j = 0;
            string = string.replace(" ", "");
            char[] lexemes = string.toCharArray();
            Relation relation = new Relation();
            while (j < lexemes.length) {
                if (lexemes[j] == '<') {
                    boolean oldNonTerminal = false;
                    if (j == 0) {
                        oldNonTerminal = true;
                    }
                    j++;
                    StringBuilder nonterminal = new StringBuilder();
                    while (lexemes[j] != '>') {
                        nonterminal.append(lexemes[j]);
                        j++;
                    }
                    if (oldNonTerminal) {
                        relation.putOldNonTerminal(new NonTerminal(nonterminal.toString()));
                        oldNonTerminal = false;
                    }
                    addNonTerminal(nonterminals, new NonTerminal(nonterminal.toString()));
                }
                j++;
            }
            relations.add(relation);
        }
        nonterminals.forEach(t -> System.out.print(t.getValue() + " "));
        System.out.println();
        relations.forEach(t -> System.out.print(t.getOldNonTerminal().getValue() + " "));
        return new Grammar(relations, nonterminals, terminals);
    }

    private void addNonTerminal(Set<NonTerminal> nonterminals, NonTerminal nonTerminal) {
        if (!(nonterminals.stream().anyMatch(t -> t.getValue().equals(nonTerminal.getValue())))) {
            nonterminals.add(nonTerminal);
        }
    }

    private void addTerminal(Set<Terminal> terminals, Terminal terminal) {
        if (!(terminals.stream().anyMatch(t -> t.getValue().equals(terminal.getValue())))) {
            terminals.add(terminal);
        }
    }
}

