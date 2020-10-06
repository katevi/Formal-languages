package vinnik.gr2automata;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        GrammarParser parser = new GrammarParser();
        Grammar grammar = parser.readGrammarFromFile();
        AutomataBuilder builder = new AutomataBuilder();
        Automata automata = builder.buildAutomata(grammar);
        String[][] tableOfTransitions = new String[automata.getInputSymbols().size() + 1][automata.getInitialStates().size() + 1];
        System.out.println("Filling table...");

        int counter = 1;
        for (InputSymbol symbol : automata.getInputSymbols()) {
            tableOfTransitions[counter][0] = symbol.getInputSymbol();
            counter++;
        }
        counter = 1;
        for (State state : automata.getInitialStates()) {
            tableOfTransitions[0][counter] = state.getStateName();
            counter++;
        }
        for (int i = 1; i < tableOfTransitions.length; i++) {
            for (int j = 1; j < tableOfTransitions[0].length; j++) {
                System.out.println();
                int finalI = i;
                int finalJ = j;
                System.out.println(tableOfTransitions[finalI][0] + " " + tableOfTransitions[0][finalJ]);
                Transition transition = automata.getTransitions().stream().filter(t ->
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

        for (int i = 0; i < tableOfTransitions.length; i++) {
            for (int j = 0; j < tableOfTransitions[0].length; j++) {
                System.out.format("%40s", tableOfTransitions[i][j]);
            }
            System.out.println();
        }
    }
}