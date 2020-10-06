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
}
