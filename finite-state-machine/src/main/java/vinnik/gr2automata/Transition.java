package vinnik.gr2automata;

import java.util.Set;

public class Transition {
    private final State oldState;
    private final Set<State> newStates;
    private final char input;

    public Transition(State oldState, char input, Set<State> newStates) {
        this.oldState = oldState;
        this.newStates = newStates;
        this.input = input;
    }

    public void addNewState(State state) {
        if (!this.newStates.stream().anyMatch(t -> t.getStateName().equals(state.getStateName()))) {
            this.newStates.add(state);
        }
    }

    public Set<State> getNewStates() {
        return this.newStates;
    }

    public char getInput() {
        return this.input;
    }
}
