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
        int separator = grammarStrings.indexOf("#");
        List<String> rules = grammarStrings.subList(0, separator);
        List<String> terminalRules = grammarStrings.subList(separator + 1, grammarStrings.size() - 1);

        Set<Terminal> terminals = new LinkedHashSet<>();
        Set<NonTerminal> nonterminals = new LinkedHashSet<>();
        Set<Relation> relations = new LinkedHashSet<>();
        StringBuilder startNonTerminal = new StringBuilder();

        int stringCounter = 0;
        for (String string : rules) {
            int j = 0;
            string = string.replace(" ", "");
            char[] lexemes = string.toCharArray();
            StringBuilder oldNonTerminal = new StringBuilder();
            while (j < lexemes.length) {
                if (lexemes[j] == '<' && j == 0) {
                    j++;
                    while (lexemes[j] != '>') {
                        oldNonTerminal.append(lexemes[j]);
                        j++;
                    }
                    j++;
                    if (stringCounter == 0) {
                        startNonTerminal.append(oldNonTerminal);
                        stringCounter++;
                    }
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
        parseTerminalRules(terminals, terminalRules);
        nonterminals.forEach(t -> System.out.print(t.getValue() + " "));
        System.out.println();
        relations.forEach(t -> System.out.println(t.getOldNonTerminal().getValue()
                + " "
                + t.getTerminal().getName() + " "
                + t.getNewNonTerminal().getValue()));
        terminals.forEach(t -> System.out.print(t.getName() + " "));
        System.out.println();
        System.out.println("Start nonterminal" + startNonTerminal);
        return new Grammar(relations, nonterminals, terminals, new NonTerminal(startNonTerminal.toString()));
    }

    private void parseTerminalRules(Set<Terminal> terminals, List<String> terminalRules) {
        for (String rule : terminalRules) {
            List<String> values = Arrays.asList(rule.split(" "));
            String valueName = values.get(0);

            Set<String> setValues = new HashSet<>(values.subList(2, values.size()));
            if (terminals.stream().anyMatch(t -> t.getName().equals(valueName))) {
                terminals.removeIf(t -> t.getName().equals(valueName));
                terminals.add(new Terminal(valueName, setValues));
            }
        }
    }

    private void addNonTerminal(Set<NonTerminal> nonterminals, NonTerminal nonTerminal) {
        if (!(nonterminals.stream().anyMatch(t -> t.getValue().equals(nonTerminal.getValue())))) {
            nonterminals.add(nonTerminal);
        }
    }

    private void addTerminal(Set<Terminal> terminals, Terminal terminal) {
        if (!(terminals.stream().anyMatch(t -> t.getName().equals(terminal.getName())))) {
            terminals.add(terminal);
        }
    }
}

