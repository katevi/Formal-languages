import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    // key is inputSymbol, value is state
    private final Map<String, String> transitions;
    private String stateName;
    private final List<String> possibleAlphabet;

    public State(String stateName, List<String> possibleAlphabet) {
        this.stateName = stateName;
        this.possibleAlphabet = possibleAlphabet;
        this.transitions = new HashMap<>();
    }

    public boolean isEquivalent(State state) {
        System.out.println(transitions.keySet());
        for (String input : possibleAlphabet) {
            System.out.println("State is " + stateName + " check with " + state.getStateName());
            System.out.println("input = " + input + " " + transitions.get(input) + " " + state.getTransitions().get(input));
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
            if (!transitions.get(input).equals(state.getTransitions().get(input))
                    /*|| state.getStateName().equals(this.getStateName())*/) {
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
        System.out.println("1 " + this.getTransitions().get(input));
        this.transitions.replace(input, renamedState);
        System.out.println("2 " + this.getTransitions().get(input));
    }

    public String getStateName() {
        return stateName;
    }


}
