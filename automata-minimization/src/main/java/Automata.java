import java.util.*;
import java.util.stream.Collectors;

public class Automata {
    private List<State> states;
    private String[][] transitionsTable;

    public Automata(String[][] transitionsTable) {
        this.states = new ArrayList<>();
        this.transitionsTable = transitionsTable;
        buildStates(transitionsTable);
    }

    public String[][] minimize() {
        removeUnreachableStates(states);
        removeEquivalentStates(states);
        return generateNewTableTransitions(transitionsTable);
    }

    private String[][] generateNewTableTransitions(String[][] transitionsTable) {
        String[][] newTable = new String[transitionsTable.length][states.size() + 1];
        int width = newTable.length;
        int height = newTable[0].length;
        // generating title for inputs
        for (int i = 0; i < width; i++) {
            newTable[i][0] = transitionsTable[i][0];
        }
        // generating title for states
        int counter = 1;
        for (State state : states) {
            newTable[0][counter] = state.getStateName();
            counter++;
        }
        // generating results of transitions
        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                int finalI = i;
                State state = states
                        .stream()
                        .filter(t -> t.getStateName().equals(newTable[0][finalI]))
                        .findFirst().orElse(null);
                if (state != null) {
                    newTable[j][i] = state.getTransitions().get(newTable[j][0]);
                }
            }
        }

        return newTable;
    }

    private void buildStates(String[][] transitionsTable) {
        int width = transitionsTable.length;
        int height = transitionsTable[0].length;
        // each state must have possible alphabet for check if it equivalent with other states
        // equivalent means that all results (for all symbols in alphabet) of transitions are the same in this states
        List<String> possibleAlphabet = generatePossibleAlphabet(transitionsTable);
        for (int j = 1; j < height; j++) {
            State state = new State(transitionsTable[0][j], possibleAlphabet);
            for (int i = 1; i < width; i++) {
                state.addTransition(transitionsTable[i][0], transitionsTable[i][j]);
                if (transitionsTable[i][j].equals("#")) {
                    state.addTransition(transitionsTable[i][0], transitionsTable[i][j]);
                 //   finalStates.add(state);
                }
            }
            states.add(state);
        }
    }

    private List<String> generatePossibleAlphabet(String[][] transitionsTable) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < transitionsTable.length; i++) {
            list.add(transitionsTable[i][0]);
        }
        return list;
    }

    private void removeUnreachableStates(List<State> states) {
        // Algorithm bfs implemented for finding unreachable states
        List<String> visitedStates = new ArrayList();
        Queue<String> unvisitedStates = new LinkedList<>();
        // Start from the first element in list, assuming that it is start symbol
        unvisitedStates.add(states.get(0).getStateName());

        while (!unvisitedStates.isEmpty()) {
            String currentStateName = unvisitedStates.poll();
            if (!visitedStates.contains(currentStateName)) {
                visitedStates.add(currentStateName);
                State state = states.stream().filter(t -> t.getStateName().equals(currentStateName)).findFirst().orElse(null);
                for (String stateName : state.getTransitions().values()) {
                    // if result of sigma operator is _ it means, that there is no transition from given state with given input
                    // and _ - is not a state
                    // # - is final state and there is no reason to add it to queue, as it is final
                    if (!visitedStates.contains(stateName) && !stateName.equals("#") && !stateName.equals("_")) {
                        unvisitedStates.add(stateName);
                    }
                }
            }
        }
        // removing all non visited in bfs (non reachable from given start state) states
        states.removeIf(t -> !visitedStates.contains(t.getStateName()));
    }

    private void removeEquivalentStates(List<State> states) {
        List<State> statesCopy = new ArrayList<>(states);

        for (State state1 : states) {
            // find all states in automata, which are equivalent to current state1 state
            List<String> equivalentStateNames = statesCopy
                    .stream()
                    .filter(t -> t.isEquivalent(state1))
                    .map(t -> t.getStateName()).collect(Collectors.toList());
            // removing all equivalent states (and even state1, since state1 is equivalent to state1
            // add one state from equivalence class to states
            // (this is implementation of merging equivalent states into one
            if (statesCopy.removeIf(t -> t.isEquivalent(state1))) {
                statesCopy.add(state1);
            }
            // if there is no equivalent states, we do not need to remove or to rename sth, so we can continue
            if (equivalentStateNames.isEmpty()) {
                continue;
            }
            // after we have found, that there is equivalence states and removed them from automata, we need to rename all
            // transitions, in which they were
            // we want to rename to name of state1, which is now representative of this equivalence class in states
            String finalName = state1.getStateName();
            for (State state : statesCopy) {
                for (String name : equivalentStateNames) {
                    // for each state we check, if it has transitions, which use some state from equivalence class
                    // and rename them
                    if (state.getTransitions().containsValue(name)) {
                        // filtering all such transitions
                        List<Map.Entry<String, String>> transitionsToRename =
                                state.getTransitions().entrySet().stream().filter(t -> t.getValue().equals(name)).collect(Collectors.toList());
                        // for each transition rename old name to name of state1
                        for (Map.Entry<String, String> transition : transitionsToRename) {
                            state.modifyTransition(transition.getKey(), finalName);
                        }
                    }
                }
            }
        }
        states.clear();
        // add modified (after removing and renaming) list of states
        states.addAll(statesCopy);
    }
}
