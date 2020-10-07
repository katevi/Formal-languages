package vinnik.gr2automata;

import java.util.Set;

public class Automata {
    private final Set<State> initialStates;
    private final Set<State> finalStates;
    private final Set<Transition> transitions;
    private final Set<InputSymbol> inputSymbols;

    public Automata(Set<State> initialStates, Set<State> finalStates, Set<Transition> transitions, Set<InputSymbol> inputSymbols) {
        this.initialStates = initialStates;
        this.finalStates = finalStates;
        this.transitions = transitions;
        this.inputSymbols = inputSymbols;
    }

    public Set<State> getInitialStates() {
        return initialStates;
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
                        t.getOldState().getStateName().equals(tableOfTransitions[0][finalJ])
                                && t.getInput().equals(tableOfTransitions[finalI][0])).findFirst().orElse(null);
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
        String[][] tableOfTransitions = new String[this.getInputSymbols().size() + 1][this.getInitialStates().size() + 1];

        int counter = 1;
        for (InputSymbol symbol : this.getInputSymbols()) {
            tableOfTransitions[counter][0] = symbol.getInputSymbol();
            counter++;
        }
        counter = 1;
        for (State state : this.getInitialStates()) {
            tableOfTransitions[0][counter] = state.getStateName();
            counter++;
        }
        return tableOfTransitions;
    }
}
