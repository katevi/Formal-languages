package vinnik.gr2automata;

import java.util.HashSet;
import java.util.Set;

public class Terminal {
    private final String name;
    private final Set<String> possibleValues;

    public Terminal(String value) {
        this.name = value;
        this.possibleValues = new HashSet<>();
        this.possibleValues.add(name);
    }

    public Terminal(String name, Set<String> possibleValues) {
        this.name = name;
        this.possibleValues = possibleValues;
    }

    public Set<String> getPossibleValues() {
        return possibleValues;
    }

    public String getName() {
        return name;
    }
}
