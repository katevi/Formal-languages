package vinnik.gr2automata;

public class Main {
    public static void main(String[] args) {
        GrammarParser parser = new GrammarParser();
        Grammar grammar = parser.readGrammarFromFile();
        AutomataBuilder builder = new AutomataBuilder();
        Automata automata = builder.buildAutomata(grammar);
        System.out.println("Final states in automata");
        automata.getFinalStates().forEach(t -> System.out.print(t.getStateName() + " "));
        System.out.println();
        System.out.println("Initial states in automata");
        automata.getInitialStates().forEach(t -> System.out.print(t.getStateName() + " "));
        System.out.println("Transition in automata");
        automata.getTransitions().forEach(t -> {
            System.out.println("old state " + t.getOldState().getStateName() + " input " + t.getInput());
            t.getNewStates().forEach(s -> System.out.print(s.getStateName() + " "));
            System.out.println();
        });
    }
}
