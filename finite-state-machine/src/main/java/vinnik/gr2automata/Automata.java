package vinnik.gr2automata;

import java.util.Set;

public class Automata {
    private final Set<State> initialStates;
    private final Set<State> finalStates;
    private final Set<Transition> transitions;

    public Automata(Set<State> initialStates, Set<State> finalStates, Set<Transition> transitions) {
        this.initialStates = initialStates;
        this.finalStates = finalStates;
        this.transitions = transitions;
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
}
