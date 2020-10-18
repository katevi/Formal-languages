public class Main {
    public static void main(String[] args) {
        TableReader tableReader = new TableReader();
        String[][] table = tableReader.readTable("table.txt");
        for (int i = 0; i < table[0].length; i++) {
            for (int j = 0; j < table.length; j++) {
                System.out.print(table[j][i]);
            }
            System.out.println();
        }
        Automata automata = new Automata(table);
        automata.minimize();
    }
}
