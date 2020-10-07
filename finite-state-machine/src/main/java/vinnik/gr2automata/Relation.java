package vinnik.gr2automata;

public class Relation {
    private final Terminal terminal;
    private final NonTerminal newNonTerminal;
    private final NonTerminal oldNonTerminal;

    public Relation(Terminal terminal, NonTerminal newNonTerminal, NonTerminal oldNonTerminal) {
        this.terminal = terminal;
        this.newNonTerminal = newNonTerminal;
        this.oldNonTerminal = oldNonTerminal;
    }

    /*public void putOldNonTerminal(NonTerminal oldNonTerminal) {
        this.oldNonTerminal = oldNonTerminal;
    }

    public void putNewNonTerminal(NonTerminal newNonTerminal) {
        this.newNonTerminal = newNonTerminal;
    }*/

    public Terminal getTerminal() {
        return terminal;
    }

    /*public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }*/

    public NonTerminal getOldNonTerminal() {
        return oldNonTerminal;
    }

    public NonTerminal getNewNonTerminal() {
        return newNonTerminal;
    }
}
