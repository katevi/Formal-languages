package vinnik.gr2automata;

import java.util.Set;

public class Grammar {
    private final Set<Relation> relations;
    private final Set<NonTerminal> nonterminals;
    private final Set<Terminal> terminals;

    public Grammar(Set<Relation> relations,
                   Set<NonTerminal> nonterminals,
                   Set<Terminal> terminals) {
        this.relations = relations;
        this.nonterminals = nonterminals;
        this.terminals = terminals;
    }
}
