package vinnik.firstfollow;

public class NonTerminal implements Token {
    private final String value;

    public NonTerminal(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "nonterminal";
    }

    @Override
    public String getValue() {
        return value;
    }
}
