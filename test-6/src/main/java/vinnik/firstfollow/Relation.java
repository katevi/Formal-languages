package vinnik.firstfollow;

import java.util.List;

public class Relation {
    private final List<Token> tokens;
    private final NonTerminal oldNonTerminal;

    public Relation(NonTerminal oldNonTerminal, List<Token> tokens) {
        this.oldNonTerminal = oldNonTerminal;
        this.tokens = tokens;
    }

    public NonTerminal getOldNonTerminal() {
        return oldNonTerminal;
    }

    public List<Token> getRightPart() {
        return tokens;
    }

    public String getRelationPrint() {
        StringBuilder result = new StringBuilder(oldNonTerminal.getValue() + "->");
        for (Token token : tokens) {
            result.append(" ");
            result.append(token.getType());
            result.append(" ");
            result.append(token.getValue());
            result.append(" ");
        }
        return result.toString();
    }
}
