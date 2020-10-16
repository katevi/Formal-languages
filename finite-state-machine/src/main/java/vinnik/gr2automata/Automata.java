package vinnik.gr2automata;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Automata {
    private final Set<State> internalStates;
    private final Set<State> finalStates;
    private final Set<Transition> transitions;
    private final Set<InputSymbol> inputSymbols;
    private final State initialState;

    public Automata(Set<State> initialStates, Set<State> finalStates, Set<Transition> transitions, Set<InputSymbol> inputSymbols, State initialState) {
        this.internalStates = initialStates;
        this.finalStates = finalStates;
        this.transitions = transitions;
        this.inputSymbols = inputSymbols;
        this.initialState = initialState;
    }


    public State getInitialState() {
        return initialState;
    }

    public Set<State> getInternalStates() {
        return internalStates;
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    public Set<InputSymbol> getInputSymbols() {
        return inputSymbols;
    }

    public String[][] buildTableOfTransitions() {
        String[][] tableOfTransitions = creteTableWithHeaders();
        tableOfTransitions[0][0] = "";
        for (int i = 1; i < tableOfTransitions.length; i++) {
            for (int j = 1; j < tableOfTransitions[0].length; j++) {
                int finalI = i;
                int finalJ = j;
                Transition transition = this.getTransitions().stream().filter(t ->
                        t.getOldState().getStateName().equals(tableOfTransitions[finalI][0])
                                && t.getInput().equals(tableOfTransitions[0][finalJ])).findFirst().orElse(null);
                StringBuilder states = new StringBuilder();
                states.append("{");
                if (transition != null) {
                    transition.getNewStates().stream().forEach(t -> states.append(t.getStateName() + ","));
                    if (transition.getNewStates().size() > 0) {
                        states.deleteCharAt(states.length() - 1);
                    }
                    states.append("}");
                    tableOfTransitions[i][j] = states.toString();
                } else {
                    tableOfTransitions[i][j] = "{}";
                }
            }
        }
        return tableOfTransitions;
    }

    private String[][] creteTableWithHeaders() {
        String[][] tableOfTransitions = new String[this.getInternalStates().size() + 1][this.getInputSymbols().size() + 1];

        int counter = 1;
        for (InputSymbol symbol : this.getInputSymbols()) {
            tableOfTransitions[0][counter] = symbol.getInputSymbolName();
            counter++;
        }
        counter = 1;
        for (State state : this.getInternalStates()) {
            tableOfTransitions[counter][0] = state.getStateName();
            counter++;
        }
        return tableOfTransitions;
    }

    public boolean recognizeWord(String word) {
        Set<State> prevDeductionStates = new LinkedHashSet<>();

        Set<State> nextDeductionStates = new LinkedHashSet<>();
        nextDeductionStates.add(initialState);

        for (int i = 0; i < word.length(); i++) {
            prevDeductionStates.clear();
            prevDeductionStates.addAll(nextDeductionStates);
            nextDeductionStates.clear();

            InputSymbol symbol = figureWhichInputSymbolValueIs(String.valueOf(word.charAt(i)));

            for (State state : prevDeductionStates) {
                Set<Transition> filteredTransitions = transitions
                        .stream()
                        .filter(t -> t.getOldState().getStateName().equals(state.getStateName())
                                && t.getInput().equals(String.valueOf(symbol.getInputSymbolName())))
                        .collect(Collectors.toSet());
                for (Transition transition : filteredTransitions) {
                    nextDeductionStates.addAll(transition.getNewStates());
                }
            }
        }

        return hasFinalState(nextDeductionStates);
    }

    private InputSymbol figureWhichInputSymbolValueIs(String symbol) {
        return inputSymbols
                .stream()
                .filter(t -> t.getPossibleValuesSet()
                        .contains(symbol)).findFirst().orElse(null);
    }

    private boolean hasFinalState(Set<State> deductedStates) {
        return deductedStates
                .stream()
                .anyMatch(t -> finalStates
                        .stream()
                        .anyMatch(s -> s.getStateName().equals(t.getStateName())));
    }
}
