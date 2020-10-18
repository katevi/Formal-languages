import java.util.HashMap;
import java.util.Map;

public class State {
    // key is inputSymbol, value is state
    private final Map<String, String> transitions;
    private final String stateName;

    public State(String stateName) {
        this.stateName = stateName;
        this.transitions = new HashMap<>();
    }

    public boolean checkIfStateEquivalent(State state) {
        for (String input : transitions.keySet()) {
            if (!transitions.get(input).equals(state.getTransitions().get(input))) {
                return false;
            }
        }
        return true;
    }

    public Map<String, String> getTransitions() {
        return transitions;
    }

    public void addTransition(String state, String input) {
        this.transitions.put(input, state);
    }

    public String getStateName() {
        return stateName;
    }


}
