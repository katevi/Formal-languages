public class Main {
    public static void main(String[] args) {
        TableReader tableReader = new TableReader();
        String[][] table = tableReader.readTable("table.txt");
        System.out.println("Table of transitions of given automata.");
        printTable(table);
        Automata automata = new Automata(table);
        String[][] minimizedTable = automata.minimize();
        System.out.println("Table of transitions of minimized automata.");
        printTable(minimizedTable);
    }

    private static void printTable(String[][] table) {
        int coefficient = 10;
        for (int i = 0; i < table[0].length; i++) {
            System.out.print(" ");
            for (int j = 0; j < table.length * coefficient; j++) {
                System.out.print("_");
            }
            System.out.println();
            for (int j = 0; j < table.length; j++) {
                System.out.format(" | %7s", table[j][i]);
            }
            System.out.println(" |");
        }
        System.out.print(" ");
        for (int j = 0; j < table.length * coefficient; j++) {
            System.out.print("_");
        }
        System.out.print("\n\n");
    }
}
