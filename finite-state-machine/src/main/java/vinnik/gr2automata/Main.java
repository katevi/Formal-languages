package vinnik.gr2automata;

public class Main {
    public static void main(String[] args) {
        GrammarParser parser = new GrammarParser();
        Grammar grammar = parser.readGrammarFromFile();
        AutomataBuilder builder = new AutomataBuilder();
        Automata automata = builder.buildAutomata(grammar);
        String[][] tableOfTransitions = automata.buildTableOfTransitions();
        System.out.println("Filling table...");
        for (int i = 0; i < tableOfTransitions.length; i++) {
            for (int j = 0; j < tableOfTransitions[0].length; j++) {
                System.out.format("%40s |", tableOfTransitions[i][j]);
            }
            System.out.println();
        }
        System.out.println(automata.recognizeWord("0"));
    }
}