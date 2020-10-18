import java.util.*;
import java.util.stream.Collectors;

public class Automata {
    private List<State> states;

    public Automata(String[][] transitionsTable) {
        states = new ArrayList<>();
        buildStates(transitionsTable);
    }

    public void minimize() {
        removeUnreachableStates(states);
        removeEquivalentStates(states);
    }

    private void buildStates(String[][] transitionsTable) {
        int width = transitionsTable.length;
        int height = transitionsTable[0].length;
        System.out.println(width + " " + height);
        for (int j = 1; j < height; j++) {
            State state = new State(transitionsTable[0][j]);
            for (int i = 1; i < width; i++) {
               if (!(transitionsTable[i][j].equals("_") || transitionsTable[i][j].equals("#"))) {
                    state.addTransition(transitionsTable[i][0], transitionsTable[i][j]);
               }
            }
            states.add(state);
        }
        states.stream().forEach(t -> System.out.println(t.getStateName() + " " + t.getTransitions()));

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
                for (String stateName : state.getTransitions().keySet()) {
                    if (!visitedStates.contains(stateName)) {
                        unvisitedStates.add(stateName);
                    }
                }
                //unvisitedStates.addAll(new ArrayList<>(state.getTransitions().keySet()));
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

    }
}
