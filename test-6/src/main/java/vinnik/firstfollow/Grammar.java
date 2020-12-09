package vinnik.firstfollow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Grammar {
    private final Set<Relation> relations;
    private final Set<NonTerminal> nonterminals;
    private final Set<Terminal> terminals;
    private final NonTerminal startNonterminal;

    public Grammar(Set<Relation> relations,
                   Set<NonTerminal> nonterminals,
                   Set<Terminal> terminals,
                   NonTerminal startNonterminal) {
        this.relations = relations;
        this.nonterminals = nonterminals;
        this.terminals = terminals;
        this.startNonterminal = startNonterminal;
    }


    public Set<Relation> getRelations() {
        return relations;
    }

    public Set<NonTerminal> getNonterminals() {
        return nonterminals;
    }

    public Set<Terminal> getTerminals() {
        return terminals;
    }

    public NonTerminal getStartNonterminal() {
        return startNonterminal;
    }

    public void calculateFirsts(int k) {
        for (NonTerminal nonTerminal : nonterminals) {
            System.out.print(nonTerminal.getValue() + " : ");
            for (List<Token> tokens : first(nonTerminal, k)) {
                for (Token token : tokens) {
                    System.out.print(token.getValue());
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public List<List<Token>> first(Token token, int k) {
        List<List<Token>> currentFirsts = new ArrayList<>();

        if (token.getType().equals("terminal")) {
            List<Token> tokens = new ArrayList<Token>();
            tokens.add(token);
            //currentFirsts.add(tokens);
            addToCurrentFirst(currentFirsts, tokens);
            return currentFirsts;
        }

        //token is nonTerminal
        NonTerminal nonTerminal = new NonTerminal(token.getValue());

        List<Relation> relations = getRelationWithGivenOldNonterminal(nonTerminal);

        for (Relation relation : relations) {
            List<Token> newFirst = new ArrayList<>();
            if (firstTokenIsTerminal(relation)) {
                if (relationHasKTerminalsFirst(relation, k)) {
                    if (k > 0) {
                        newFirst.addAll(relation.getRightPart().subList(0, k));
                    }
                }
                if (relationHasLessThanKTerminalsAndEps(relation, k)) {
                    int lengthOfRightPart = relation.getRightPart().size();
                    newFirst.addAll(relation.getRightPart().subList(0, lengthOfRightPart));
                }
                addToCurrentFirst(currentFirsts, newFirst);
            } else {
                for (Token potentialFirst : relation.getRightPart()) {
                    for (List<Token> first : first(potentialFirst, k)) {
                        addToCurrentFirst(currentFirsts, first);
                    }
                }
            }
        }
        return currentFirsts;
    }

    private boolean firstTokenIsTerminal(Relation relation) {
        return relation.getRightPart().get(0).getType().equals("terminal");
    }

    private void addToCurrentFirst(List<List<Token>> currentFirsts, List<Token> newFirst) {
        if (currentFirsts.stream().noneMatch(t -> firstsEqual(t, newFirst))) {
            currentFirsts.add(newFirst);
        }
    }

    private boolean firstsEqual(List<Token> tokens1, List<Token> tokens2) {
        if (tokens1.size() != tokens2.size()) return false;
        Token[] arr1 = new Token[tokens1.size()];
        tokens1.toArray(arr1);
        Token[] arr2 = new Token[tokens2.size()];
        tokens2.toArray(arr2);
        for (int i = 0; i < arr1.length; i++) {
            if (!arr1[i].getValue().equals(arr2[i].getValue())) {
                return false;
            }
        }
        return true;
    }

    // Two checks below can be moved in one method, but such implementation is more readable
    private boolean relationHasLessThanKTerminalsAndEps(Relation relation, int k) {
        System.out.println(relation.getRelationPrint() + "1");
        Token[] tokens = new Token[relation.getRightPart().size()];
        relation.getRightPart().toArray(tokens);

        if (tokens.length > k) return false;
        System.out.println(relation.getRelationPrint() + "2");
        // do not need to add case, when tokens.length < k, since it is already true

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].getType().equals("nonterminal")) {
                System.out.println(relation.getRelationPrint() + "3");
                return false;
            }
        }
        System.out.println(relation.getRelationPrint() + "4");
        return true;
    }

    private boolean relationHasKTerminalsFirst(Relation relation, int k) {
        int size = relation.getRightPart().size();
        if (k > size) return false;
        Token[] tokens = new Token[size];
        relation.getRightPart().toArray(tokens);
        for (int i = 0; i < k; k++) {
            if (tokens[i].getType().equals("nonterminal")) {
                return false;
            }
        }
        return true;
    }

    private List<Relation> getRelationWithGivenOldNonterminal(NonTerminal nonterminal) {
        return relations
                .stream()
                .filter(t -> t
                        .getOldNonTerminal()
                        .getValue()
                        .equals(nonterminal.getValue()))
                .collect(Collectors.toList());
    }
}
