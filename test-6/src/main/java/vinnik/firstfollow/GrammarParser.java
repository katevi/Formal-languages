package vinnik.firstfollow;

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
        Set<Relation> relations = new HashSet<>();
        Set<Terminal> terminals = new HashSet<>();
        Set<NonTerminal> nonterminals = new HashSet<>();
        StringBuilder startNonTerminal = new StringBuilder();

        int stringCounter = 0;
        for (String string : grammarStrings) {
            String[] tokens = string.split(" ");

            String oldNonterminal = tokens[0];
            if (stringCounter == 0) {
                startNonTerminal.append(oldNonterminal);
                nonterminals.add(new NonTerminal(oldNonterminal));
                stringCounter++;
            }

            List<Token> rightPart = new ArrayList<>();

            for (int i = 1; i < tokens.length; i++) {
                Token token;
                if (isNonterminal(tokens[i])) {
                    NonTerminal nonterminal = new NonTerminal(tokens[i]);
                    addNonTerminal(nonterminals, nonterminal);
                    token = nonterminal;
                    rightPart.add(token);
                }
                if (isTerminal(tokens[i])) {
                    Terminal terminal = new Terminal(tokens[i]);
                    addTerminal(terminals, terminal);
                    token = terminal;
                    rightPart.add(token);
                }
            }
            Relation relation = new Relation(new NonTerminal(oldNonterminal), rightPart);
            relations.add(relation);
        }

        return new Grammar(relations, nonterminals, terminals, new NonTerminal(startNonTerminal.toString()));
    }

    private boolean isNonterminal(String token) {
        return token.matches("[A-Z]+");
    }

    private boolean isTerminal(String token) {
        return token.matches("[a-z]+");
    }

    private boolean isEpsilon(String token) {
        return token.matches("Eps");
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

