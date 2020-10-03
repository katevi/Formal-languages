package vinnik.gr2automata;

public class Relation {
    private Terminal terminal;
    private NonTerminal newNonTerminal;
    private NonTerminal oldNonTerminal;

    public void putOldNonTerminal(NonTerminal oldNonTerminal) {
        this.oldNonTerminal = oldNonTerminal;
    }

    public void putNewNonTerminal(NonTerminal newNonTerminal) {
        this.newNonTerminal = newNonTerminal;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public NonTerminal getOldNonTerminal() {
        return oldNonTerminal;
    }

    public NonTerminal getNewNonTerminal() {
        return newNonTerminal;
    }
}
