package vinnik.firstfollow;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private final Set<Relation> relations;
    private final Set<NonTerminal> nonterminals;
    private final Set<Terminal> terminals;
    private final NonTerminal startNonterminal;
    private Map<String, List<Set<String>>> firsts = new HashMap<>();
    private Map<Pair, List<Set<String>>> follows = new HashMap<>();

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

    // FIRST PART
    public void calculateFirsts(int k) {
        firsts = first(k);
        System.out.println("First:");
        for (String nonterminal : firsts.keySet()) {
            int size = firsts.get(nonterminal).size();
            System.out.print(nonterminal + ": ");
            System.out.println(firsts.get(nonterminal).get(size - 1));
        }
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
                    nonterminalF0.add(word);
                    continue;
                }
                int amountOfTerminals = relationHasLessThanKTerminalsAndEps(relation, k);
                if (amountOfTerminals != -1) {
                    String word = relation.getRightPart().subList(0, amountOfTerminals)
                            .stream().map(Token::getValue).collect(Collectors.joining());
                    nonterminalF0.add(word);
                    continue;
                }
            }
            firsts.get(nonterminal).add(nonterminalF0);
        }

        // building other F_i
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
                        setsForCartesianProduct.add(firsts.get(token.getValue()).get(size - 1));
                    }
                    Set<List<String>> cartesianSet = Sets.cartesianProduct(setsForCartesianProduct);
                    Set<String> joinedCartesianSet = cartesianSet.stream().map(t -> String.join("", t)).collect(Collectors.toSet());
                    joinedCartesianSet = joinedCartesianSet.stream().map(t -> {
                        if (t.length() > k) {
                            return t.substring(0, k);
                        }
                        return t;
                    }).collect(Collectors.toSet());
                    F_i = Sets.union(F_i, joinedCartesianSet);
                    // maybe move this union out of loop? since it not depends on relation
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
        List<Token> tokens = relation.getRightPart();

        if (tokens.size() > k) return -1;
        // do not need to add case, when tokens.length < k, since it is already true

        if (tokens.stream().anyMatch(t -> t.getType().equals("nonterminal"))) {
            return -1;
        }
        return tokens.size();
    }

    private boolean relationHasKTerminalsFirst(Relation relation, int k) {
        int size = relation.getRightPart().size();
        if (k > size) return false;
        Token[] tokens = new Token[size];
        relation.getRightPart().toArray(tokens);
        for (int i = 0; i < k; i++) {
            if (tokens[i].getType().equals("nonterminal")) {
                return false;
            }
        }
        return true;
    }

    // FOLLOW PART
    public void calculateFollows(int k) {
        follows = follow(k);
        System.out.println("Follow:");
        for (Pair key : follows.keySet()) {
            //if (key.getFirst().equals(startNonterminal.getValue())) {
                int size = follows.get(key).size();
                System.out.print(key.getFirst() + " " + key.getSecond() + ": ");
                System.out.println(follows.get(key));
            //}
        }
    }

    private Map<Pair, List<Set<String>>> follow(int k) {
        Map<Pair, List<Set<String>>> follows = new HashMap<>();
        for (NonTerminal nonTerminal1 : nonterminals) {
            for (NonTerminal nonTerminal2 : nonterminals) {
                Pair key = new Pair(nonTerminal1.getValue(), nonTerminal2.getValue());
                follows.put(key, new ArrayList<>());
            }
        }

        // init Phi_0(X,Y)
        for (Pair key : follows.keySet()) {
            Set<String> PHI_0 = new HashSet<>();

            List<Relation> relations = getRelationWithGivenOldAndNewNonterminal(new NonTerminal(key.getFirst()), new NonTerminal(key.getSecond()));

            for (Relation relation : relations) {
                List<List<Token>> rightParts = getAllAfterNewNonterminal(relation, new NonTerminal(key.getSecond()));
                for (List<Token> rightPart : rightParts) {
                    // IT IS FIRST(alpha), where alpha = word, so, FIRST(ALPHA) = subword of alpha with length k
                    List<Set<String>> setsForCartesianProduct = new ArrayList<>();
                    for (Token token : rightPart) {
                        if (token.getType().equals("terminal")) {
                            Set<String> set = new HashSet<>();
                            set.add(token.getValue());
                            setsForCartesianProduct.add(set);
                            continue;
                        }
                        int size = firsts.get(token.getValue()).size();
                        setsForCartesianProduct.add(firsts.get(token.getValue()).get(size - 1));
                    }

                    Set<String> joinedCartesianSet = cartesianProductAndJoin(setsForCartesianProduct, k);
                    PHI_0 = Sets.union(PHI_0, joinedCartesianSet);
                }
            }
            follows.get(key).add(PHI_0);
        }

        // starting iterations while PHI_i != PHI_i+1 for all pairs of nonterminals (A,B)
        boolean followsAllSetsRepeated = false;
        while (!followsAllSetsRepeated) {
            for (Pair key : follows.keySet()) {
                int size = follows.get(key).size();

                Set<String> PHI_previous_i = follows.get(key).get(size - 1);
                List<Relation> relations = getRelationWithGivenOldNonterminal(new NonTerminal(key.getFirst()));
                // it is new iteration for current pair
                Set<String> PHI_i = new HashSet<>();
                Set<String> resultSet = new HashSet<>();
                for (Relation relation : relations) {
                    for (Token token1 : relation.getRightPart()) {
                        List<Set<String>> setsForCartesianProduct = new ArrayList<>();
                        if (!token1.getType().equals("nonterminal")) continue;

                        // if token is nonterminal then
                        Pair pair =  follows.keySet().stream().filter(t -> t.getFirst().equals(token1.getValue())
                                && t.getSecond().equals(key.getSecond())).findFirst().orElse(null);
                        int sizeOfListForPair = follows.get(pair).size();
                        Set<String> PHI_I_token1_second = follows.get(pair).get(sizeOfListForPair - 1);

                        setsForCartesianProduct.add(PHI_I_token1_second);

                        int index = relation.getRightPart().indexOf(token1);
                        int rightPartSize = relation.getRightPart().size();
                        List<Token> tokens = relation.getRightPart().subList(index + 1, rightPartSize);
                        for (Token nextToken : tokens) {
                                // first for terminal is terminal
                            if (nextToken.getType().equals("terminal")) {
                                Set<String> nextSetForCartesianProduct = new HashSet<>();
                                nextSetForCartesianProduct.add(nextToken.getValue());
                                setsForCartesianProduct.add(nextSetForCartesianProduct);
                                continue;
                            }
                            // if nonterminal - then take value of first from already calculated firsts
                            int lastFirstIterationNumber = firsts.get(nextToken.getValue()).size();
                            setsForCartesianProduct.add(firsts.get(nextToken.getValue()).get(lastFirstIterationNumber - 1));
                        }
                        // if tokens is empty - then we do not need to add to cartesian product
                        Set<String> joinedCartesianSet = cartesianProductAndJoin(setsForCartesianProduct, k);
                        PHI_i = Sets.union(PHI_i, joinedCartesianSet);
                        // move out of loop?
                        PHI_i = Sets.union(PHI_i, PHI_previous_i);
                    }
                }
                follows.get(key).add(PHI_i);
            }
            followsAllSetsRepeated = followsAllSetsRepeated(follows);
        }
        return follows;
    }

    private Set<String> cartesianProductAndJoin(List<Set<String>> setsForCartesianProduct, int k) {
        Set<List<String>> cartesianSet = Sets.cartesianProduct(setsForCartesianProduct);
        Set<String> joinedCartesianSet = cartesianSet.stream().map(t -> String.join("", t)).collect(Collectors.toSet());
        joinedCartesianSet = joinedCartesianSet.stream().map(t -> {
            if (t.length() > k) {
                return t.substring(0, k);
            }
            return t;
        }).collect(Collectors.toSet());
        return joinedCartesianSet;
    }

    private boolean followsAllSetsRepeated(Map<Pair, List<Set<String>>> follows) {
        for (Pair key : follows.keySet()) {
            int size = follows.get(key).size();
            if (!setRepeated(follows.get(key).get(size - 1), follows.get(key).get(size - 2))) {
                return false;
            }
        }
        return true;
    }

    private static class Pair {
        private final String first;
        private final String second;
        public Pair(String first, String second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }
    }


    // HELPER METHODS
    private List<Relation> getRelationWithGivenOldNonterminal(NonTerminal nonterminal) {
        return relations
                .stream()
                .filter(t -> t
                        .getOldNonTerminal()
                        .getValue()
                        .equals(nonterminal.getValue()))
                .collect(Collectors.toList());
    }

    // only rules kind of A -> alpha B gamma, where alpha and gamma are in V* (words, or empty words) and
    // A - oldNonterminal, B - newnonterminal
    private List<Relation> getRelationWithGivenOldAndNewNonterminal(NonTerminal oldNonterminal, NonTerminal newNonterminal) {
        return relations
                .stream()
                .filter(t -> t.getOldNonTerminal().getValue().equals(oldNonterminal.getValue())
                        && t.getRightPart().stream().anyMatch(s -> s.getValue().equals(newNonterminal.getValue())))
                .collect(Collectors.toList());
    }

    private List<List<Token>> getAllAfterNewNonterminal(Relation relation, NonTerminal newNonterminal) {
        List<List<Token>> result = new ArrayList<>();
        List<Token> tokens = relation.getRightPart();
        for (Token token : tokens) {
            if (token.getValue().equals(newNonterminal.getValue())) {
                int index = tokens.indexOf(token);
                result.add(relation.getRightPart().subList(index + 1, tokens.size()));
            }
        }
        return result;
    }
}
