import java.util.*;
import java.util.stream.Collectors;

public class Automata {
    private List<State> states;
    private List<State> finalStates;
    private String[][] transitionsTable;

    public Automata(String[][] transitionsTable) {
        states = new ArrayList<>();
        finalStates = new ArrayList<>();
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
        for (int i = 0; i < width; i++) {
            newTable[i][0] = transitionsTable[i][0];
        }
        int counter = 1;
        for (State state : states) {
            newTable[0][counter] = state.getStateName();
            counter++;
        }
        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                int finalI = i;
                State state = states
                        .stream()
                        .filter(t -> t.getStateName().equals(newTable[0][finalI]))
                        .findFirst().orElse(null);
                if (state != null) {
                    System.out.println("input = " + newTable[j][0] + " , state  = " + newTable[0][i]);
                    System.out.println(state.getTransitions().get(newTable[j][0]));
                    newTable[j][i] = state.getTransitions().get(newTable[j][0]);
                }
            }
        }
        finalStates.stream().forEach(t -> System.out.println(t.getStateName() + " "));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.format("%10s", newTable[j][i]);
            }
            System.out.println();
        }

        return newTable;
    }

    private void buildStates(String[][] transitionsTable) {
        int width = transitionsTable.length;
        int height = transitionsTable[0].length;
        List<String> possibleAlphabet = generatePossibleAlphabet(transitionsTable);
        for (int j = 1; j < height; j++) {
            State state = new State(transitionsTable[0][j], possibleAlphabet);
            for (int i = 1; i < width; i++) {
               if (!(transitionsTable[i][j].equals("_") || transitionsTable[i][j].equals("#"))) {
                    state.addTransition(transitionsTable[i][0], transitionsTable[i][j]);
               } else {
                   if (transitionsTable[i][j].equals("#")) {
                       state.addTransition(transitionsTable[i][0], transitionsTable[i][j]);
                       finalStates.add(state);
                   }
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
            System.out.println("current state is = " + currentStateName);
            if (!visitedStates.contains(currentStateName)) {
                System.out.println("current state is = " + currentStateName + " and it was not in visited");
                visitedStates.add(currentStateName);
                State state = states.stream().filter(t -> t.getStateName().equals(currentStateName)).findFirst().orElse(null);
                System.out.println("new states in queue = "
                            + new ArrayList<>(state.getTransitions().keySet()));
                for (String stateName : state.getTransitions().values()) {
                    if (!visitedStates.contains(stateName) && !stateName.equals("#")) {
                        unvisitedStates.add(stateName);
                    }
                }
                System.out.println("Queue now = " + unvisitedStates);
            }
        }
        System.out.print("visited states = ");
        visitedStates.forEach(t -> System.out.print(t + " "));
        System.out.println();
        states.forEach(t -> System.out.print(t.getStateName() + " "));
        System.out.println(visitedStates.contains("q2"));
        states.removeIf(t -> !visitedStates.contains(t.getStateName()));
        System.out.println();
        states.forEach(t -> System.out.print(t.getStateName() + " "));
    }

    private void removeEquivalentStates(List<State> states) {
        System.out.println("In equivalence");
        states.forEach(t -> System.out.print(t.getStateName() + " "));
        List<State> statesCopy = new ArrayList<>(states);

        for (State state1 : states) {
            List<String> equivalentStateNames = statesCopy
                    .stream()
                    .filter(t -> t.isEquivalent(state1))
                    .map(t -> t.getStateName()).collect(Collectors.toList());
            System.out.println("Equivalent state names");
            equivalentStateNames.forEach(t -> System.out.print(t + " "));
            if (statesCopy.removeIf(t -> t.isEquivalent(state1))) {
                statesCopy.add(state1);
                /*finalStates.removeIf(t -> t.isEquivalent(state1));
                finalStates.add(state1);*/
            }
            if (equivalentStateNames.isEmpty()) {
                continue;
            }
            String finalName = state1.getStateName();
            System.out.println("FINAL NAME = " + finalName);
            for (State state : statesCopy) {
                for (String name : equivalentStateNames) {
                    if (state.getTransitions().containsValue(name)) {
                        List<Map.Entry<String, String>> transitionsToRename =
                                state.getTransitions().entrySet().stream().filter(t -> t.getValue().equals(name)).collect(Collectors.toList());
                        for (Map.Entry<String, String> transition : transitionsToRename) {
                            System.out.println("Renaming : " + transition.getKey() + " " + transition.getValue());
                            System.out.println(state.getTransitions());
                            state.modifyTransition(transition.getKey(), finalName);
                            System.out.println("Modified transitions " + state.getTransitions());
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println();
        states.forEach(t -> System.out.print(t.getStateName() + " "));
        System.out.println();
        statesCopy.forEach(t -> System.out.print(t.getStateName() + " "));
        states.clear();
        states.addAll(statesCopy);
        states.forEach(t -> System.out.print(t.getTransitions() + " "));
    }
}
