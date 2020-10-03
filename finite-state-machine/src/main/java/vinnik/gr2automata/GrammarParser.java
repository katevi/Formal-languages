package vinnik.gr2automata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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
            //Relation relation = new Relation();
            StringBuilder oldNonTerminal = new StringBuilder();
            while (j < lexemes.length) {
                if (lexemes[j] == '<' && j == 0) {
                    j++;
                    while (lexemes[j] != '>') {
                        oldNonTerminal.append(lexemes[j]);
                        j++;
                    }
                    j++;
                    addNonTerminal(nonterminals, new NonTerminal(oldNonTerminal.toString()));
                }
                if (lexemes[j] == ':' && lexemes[j + 1] == ':' && lexemes[j + 2] == '=') {
                    j = j + 3;

                    String rightPartOfRule = string.substring(j);
                    String[] rightPartsOfRelation = rightPartOfRule.split("\\|");

                    for (String i : rightPartsOfRelation) {

                        char[] lexemesOfRulePart = i.toCharArray();
                        int k = 0;
                        StringBuilder terminal = new StringBuilder();
                        StringBuilder newNonTerminal = new StringBuilder();

                        while (k < lexemesOfRulePart.length) {
                            while (k < lexemesOfRulePart.length && lexemesOfRulePart[k] != '<') {
                                terminal.append(lexemesOfRulePart[k]);
                                k++;
                            }
                            addTerminal(terminals, new Terminal(terminal.toString()));
                            if (k < lexemesOfRulePart.length && lexemesOfRulePart[k] == '<') {
                                k++;
                                while (lexemesOfRulePart[k] != '>') {
                                    newNonTerminal.append(lexemesOfRulePart[k]);
                                    k++;
                                }
                                k++;
                            }
                        }
                        Relation relation = new Relation(
                                new Terminal(terminal.toString()),
                                new NonTerminal(newNonTerminal.toString()),
                                new NonTerminal(oldNonTerminal.toString()));
                        relations.add(relation);
                    }
                }
                j++;
            }
        }
        nonterminals.forEach(t -> System.out.print(t.getValue() + " "));
        System.out.println();
        relations.forEach(t -> System.out.println(t.getOldNonTerminal().getValue()
                + " "
                + t.getTerminal().getValue() + " "
                + t.getNewNonTerminal().getValue()));
        terminals.forEach(t -> System.out.print(t.getValue() + " "));
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

