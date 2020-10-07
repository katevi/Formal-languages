package vinnik.gr2automata;

import java.util.*;
import java.util.stream.Collectors;
public class AutomataBuilder {
    private final static String EPSILON_STATE = "epsilon";

    private final State attachedState = new State("AState");

    public Automata buildAutomata(Grammar grammar) {

        Set<State> initialStates = buildInitalStates(grammar);
        Set<State> finalStates = buildFinalStates(grammar, attachedState);
        Set<Transition> transitions = buildTransitions(grammar);
        Set<InputSymbol> symbols = buildInputSymbols(grammar);

        return new Automata(initialStates, finalStates,transitions, symbols);
    }

    private Set<InputSymbol> buildInputSymbols(Grammar grammar) {
        return grammar.getTerminals().stream().map(t -> new InputSymbol(t.getValue())).collect(Collectors.toSet());
    }

    private Set<Transition> buildTransitions(Grammar grammar) {
        Set<Transition> result = new LinkedHashSet<>();
        for (NonTerminal nonterminal : grammar.getNonterminals()) {
            for (Terminal terminal : grammar.getTerminals()) {
                // received nonterminals which are reachable from current state with given input
                Set<State> newStates = calculateNewStatesInTransition(grammar, nonterminal, terminal);
                result.add(new Transition(
                        new State(nonterminal.getValue()),
                        terminal.getValue(),
                        newStates));
            }
        }
        return result;
    }

    private Set<State> calculateNewStatesInTransition(Grammar grammar, NonTerminal nonTerminal, Terminal terminal) {
        List<NonTerminal> possibleNonTerminals = grammar
                .getRelations()
                .stream()
                .filter(t -> t.getOldNonTerminal().getValue().equals(nonTerminal.getValue()) &&
                        t.getTerminal().getValue().equals(terminal.getValue()))
                .map(Relation::getNewNonTerminal)
                .collect(Collectors.toList());
        Set<State> newStates = possibleNonTerminals
                .stream()
                .map(t -> new State(t.getValue()))
                .collect(Collectors.toSet());
        if (hasEmptyState(newStates)) {
            newStates.removeIf(t -> t.getStateName().equals(""));
            if (terminal.getValue().equals(EPSILON_STATE)) {
                newStates.add(new State(nonTerminal.getValue()));
            } else {
                newStates.add(attachedState);
            }
        }
        return newStates;
    }

    private boolean hasEmptyState(Set<State> newStates) {
        return newStates.stream().anyMatch(t -> t.getStateName().equals(""));
    }

    private Set<State> buildInitalStates(Grammar grammar) {
        Set<State> result = new LinkedHashSet<>();
        result
                .addAll(grammar
                .getNonterminals()
                .stream()
                .map(t -> new State(t.getValue())).collect(Collectors.toList()));
        result.add(attachedState);
        return result;
    }

    private Set<State> buildFinalStates(Grammar grammar, State attachedState) {
        Set<State> result = new LinkedHashSet<>();
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
                .anyMatch(t -> (t.getTerminal().getValue().equals(EPSILON_STATE) &&
                        t.getOldNonTerminal().getValue().equals(grammar.getStartNonterminal().getValue())));
    }
}
