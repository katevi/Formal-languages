package vinnik.gr2automata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AutomataBuilder {
    private final State attachedState = new State("AState");

    public Automata buildAutomata(Grammar grammar) {

        Set<State> initialStates = buildInitalStates(grammar);
        Set<State> finalStates = buildFinalStates(grammar, attachedState);
        Set<Transition> transitions = new HashSet<>();

        Automata automata = new Automata(initialStates, finalStates,transitions);
        return automata;
    }

    private Set<State> buildInitalStates(Grammar grammar) {
        Set<State> result = new HashSet<>();
        result
                .addAll(grammar
                .getNonterminals()
                .stream()
                .map(t -> new State(t.getValue())).collect(Collectors.toList()));
        result.add(attachedState);
        return result;
    }

    private Set<State> buildFinalStates(Grammar grammar, State attachedState) {
        Set<State> result = new HashSet<>();
        if (existsSpecialRelation(grammar)) {
            result.addAll(Arrays.asList(attachedState, new State(grammar.getStartNonterminal().getValue())));
            return result;
        }
        result.add(attachedState);
        return result;
    }

    private boolean existsSpecialRelation(Grammar grammar) {
        return
        grammar
                .getRelations()
                .stream()
                .anyMatch(t -> (t.getTerminal().getValue().equals("epsilon") &&
                        t.getOldNonTerminal().getValue().equals(grammar.getStartNonterminal().getValue())));
    }
}
