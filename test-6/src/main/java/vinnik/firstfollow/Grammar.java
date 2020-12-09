package vinnik.firstfollow;

import com.google.common.collect.Sets;

import java.nio.file.FileSystemNotFoundException;
import java.util.*;
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
        Map<String, List<Set<String>>> firsts = first(k);
        System.out.println("Results:");
        for (String nonterminal : firsts.keySet()) {
            System.out.println(firsts.get(nonterminal));
        }
        /*for (NonTerminal nonTerminal : nonterminals) {
            System.out.print(nonTerminal.getValue() + " : ");
         //   for (List<Token> tokens : first(nonTerminal, k)) {
           //     for (Token token : tokens) {
             //       System.out.print(token.getValue());
              //  }
                /System.out.print(" ");
            //}
            //System.out.println();
        }*/
    }

    public Map<String, List<Set<String>>> first(int k) {
        Map<String, List<Set<String>>> firsts = new HashMap();

        // init
        for (NonTerminal nonterminal : nonterminals) {
            firsts.put(nonterminal.getValue(), new ArrayList<>());
        }

        // initialization of F0
        for (String nonterminal : firsts.keySet()) {

            Set<String> nonterminalF0 = new HashSet<>();
            NonTerminal nonterminal1 = new NonTerminal(nonterminal);

            List<Relation> relations = getRelationWithGivenOldNonterminal(nonterminal1);
            for (Relation relation : relations) {
                if (relationHasKTerminalsFirst(relation, k)) {
                    String word = relation.getRightPart().subList(0, k).stream().map(Token::getValue).collect(Collectors.joining());
                    System.out.println("new word in set " + nonterminal1.getValue() + " " + word);
                    nonterminalF0.add(word);
                    continue;
                }
                int amountOfTerminals = relationHasLessThanKTerminalsAndEps(relation, k);
                if (amountOfTerminals != -1) {
                    String word = relation.getRightPart().subList(0, amountOfTerminals)
                            .stream().map(Token::getValue).collect(Collectors.joining());
                    System.out.println("new word in set" + nonterminal1.getValue() + " " + word);
                    nonterminalF0.add(word);
                    continue;
                }
            }
            firsts.get(nonterminal).add(nonterminalF0);
        }

        // building other F_i
        int iteration = 1;
        boolean allSetsRepeated = false;
        while (!allSetsRepeated) {
            for (String nonterminal : firsts.keySet()) {
                Set<String> F_i = new HashSet<>();
                int size = firsts.get(nonterminal).size();
                Set<String> F_previous_i = firsts.get(nonterminal).get(size - 1);

                NonTerminal nonterminal1 = new NonTerminal(nonterminal);
                List<Relation> relations = getRelationWithGivenOldNonterminal(nonterminal1);
                for (Relation relation : relations) {
                    List<Set<String>> setsForCartesianProduct = new ArrayList<>();
                    for (Token token : relation.getRightPart()) {
                        if (token.getType().equals("terminal")) {
                            Set<String> set = new HashSet<>();
                            set.add(token.getValue());
                            setsForCartesianProduct.add(set);
                            continue;
                        }
                        System.out.println("value of token = " + token.getValue());
                        System.out.println(firsts.get(token.getValue()));
                        setsForCartesianProduct.add(firsts.get(token.getValue()).get(size - 1));
                    }
                    Set<List<String>> cartesianSet = Sets.cartesianProduct(setsForCartesianProduct);
                    Set<String> joinedCartesianSet = cartesianSet.stream().map(t -> String.join("", t)).collect(Collectors.toSet());
                    joinedCartesianSet.stream().forEach(t -> System.out.print(t + " "));
                    joinedCartesianSet.removeIf(t -> (t.length() > k));
                    System.out.println();
                    F_i = Sets.union(F_i, joinedCartesianSet);
                    F_i = Sets.union(F_i, F_previous_i);
                }
                firsts.get(nonterminal).add(F_i);
            }
            allSetsRepeated = allSetsRepeated(firsts);
        }
        return firsts;
    }

    private boolean allSetsRepeated(Map<String, List<Set<String>>> firsts) {
        for (String nonterminal : firsts.keySet()) {
            int size = firsts.get(nonterminal).size();
            if (!setRepeated(firsts.get(nonterminal).get(size - 1), firsts.get(nonterminal).get(size - 2))) {
                return false;
            }
        }
        return true;
    }

    private boolean setRepeated(Set<String> set1, Set<String> set2) {
        return set1.equals(set2);
    }

    // Two checks below can be moved in one method, but such implementation is more readable
    private int relationHasLessThanKTerminalsAndEps(Relation relation, int k) {
        //System.out.println(relation.getRelationPrint() + "1");
        //Token[] tokens = new Token[relation.getRightPart().size()];
        List<Token> tokens = relation.getRightPart();

        if (tokens.size() > k) return -1;
        //System.out.println(relation.getRelationPrint() + "2");
        // do not need to add case, when tokens.length < k, since it is already true

        if (tokens.stream().anyMatch(t -> t.getType().equals("nonterminal"))) {
            return -1;
        }
        //System.out.println(relation.getRelationPrint() + "4");
        return tokens.size();
    }

    private boolean relationHasKTerminalsFirst(Relation relation, int k) {
        int size = relation.getRightPart().size();
        if (k > size) return false;
        Token[] tokens = new Token[size];
        relation.getRightPart().toArray(tokens);
        for (int i = 0; i < k; i++) {
           // System.out.println("CURRENT TOKEN IS " + tokens[i].getValue() + " " + tokens[i].getType());
            if (tokens[i].getType().equals("nonterminal")) {
                return false;
            }
        }
        //System.out.println(relation.getRelationPrint() + " " + true);
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
