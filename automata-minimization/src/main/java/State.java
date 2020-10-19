import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    // key is inputSymbol, value is state
    private final Map<String, String> transitions;
    private String stateName;
    private final List<String> possibleAlphabet;
    private boolean isFinal = false;

    public void setFinal() {
        isFinal = true;
    }

    public State(String stateName, List<String> possibleAlphabet) {
        this.stateName = stateName;
        this.possibleAlphabet = possibleAlphabet;
        this.transitions = new HashMap<>();
    }

    public boolean isEquivalent(State state) {
        for (String input : possibleAlphabet) {
            if (transitions.get(input) == null) {
                if (state.getTransitions().get(input) != null) {
                    return false;
                } else {
                    continue;
                }
            }
            if (state.getTransitions().get(input) == null) {
                if (transitions.get(input) != null) {
                    return false;
                } else {
                    continue;
                }
            }
            if (!transitions.get(input).equals(state.getTransitions().get(input))) {
                return false;
            }
        }
        return true;
    }

    public void setStateName(String newStateName) {
        stateName = newStateName;
    }

    public Map<String, String> getTransitions() {
        return transitions;
    }

    public void addTransition(String input, String state) {
        this.transitions.put(input, state);
    }

    public void modifyTransition(String input, String renamedState) {
        this.transitions.replace(input, renamedState);
    }

    public String getStateName() {
        return stateName;
    }


}
