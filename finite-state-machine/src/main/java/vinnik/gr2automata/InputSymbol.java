package vinnik.gr2automata;

import java.util.HashSet;
import java.util.Set;

public class InputSymbol {
    private final String inputSymbolName;

    private final Set<String> possibleValuesSet;

    public InputSymbol(String inputSymbol) {
        this.inputSymbolName = inputSymbol;
        this.possibleValuesSet = new HashSet<>();
    }

    public InputSymbol(String inputSymbol, Set<String> possibleValues) {
        this.inputSymbolName = inputSymbol;
        this.possibleValuesSet = possibleValues;
    }

    public String getInputSymbolName() {
        return inputSymbolName;
    }

    public Set<String> getPossibleValuesSet() {
        return possibleValuesSet;
    }
}
