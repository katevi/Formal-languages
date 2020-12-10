package vinnik.firstfollow;

import java.util.Set;

public class Terminal implements Token {
    private final String value;

    public Terminal(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "terminal";
    }

    @Override
    public String getValue() {
        return value;
    }
}
