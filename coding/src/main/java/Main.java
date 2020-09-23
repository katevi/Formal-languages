
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static int terminalsCounter = 51;
    private static int nonTerminalsCounter = 11;
    private static int semanticsCounter = 101;
    private final static Map<Integer, String> dictionarySeparators = new HashMap<>();
    private final static Map<Integer, String> dictionaryTerminalsInCommas = new HashMap<>();
    private final static Map<Integer, String> dictionaryTerminals = new HashMap<>();
    private final static Map<Integer, String> dictionaryNonTerminals = new HashMap<>();
    private final static Map<Integer, String> dictionarySemantics = new HashMap<>();

    public static void main(String[] args) {
        Stream<String> fileContent;
        String text = "";
        System.out.println("Welcome to the Coding! Please, name the file with grammar \"expression.txt\" " +
                "and put it at the same folder, as exe.");
        try {
            fileContent = Files.lines(Paths.get("expression.txt"));
            List<String> grammarStrings = fileContent.collect(Collectors.toList());
            int counter = 0;
            for (String i : grammarStrings) {
                if (counter == grammarStrings.size() - 1) {
                    text = text + i;
                } else {
                    text = text + i + "\n";
                }
                counter++;
            }
            if (!text.contains("Eofgram")) {
                System.out.println("Invalid format of input: there is no Eofgram.");
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.println("File expression.txt not found.");
        }
        String codedGrammar = codeGrammar(text);
        System.out.println("Result of grammar coding:");
        System.out.print(codedGrammar);
    }

    public static String codeGrammar(String text) {
        initializeCodesOfLexemes();
        char[] lexemes = text.toCharArray();

        parseGrammar(lexemes);
        String codedGrammar = codeGrammarInternally(text);
        System.out.println("During coding next dictionaries were build:");
        printDictionaries();
        cleanDictionaries();
        return codedGrammar;
    }

    private static void initializeCodesOfLexemes() {
        dictionarySeparators.put(1, ":");
        dictionarySeparators.put(2, "(");
        dictionarySeparators.put(3, ")");
        dictionarySeparators.put(4, ".");
        dictionarySeparators.put(5, "*");
        dictionarySeparators.put(6, ";");
        dictionarySeparators.put(7, ",");
        dictionarySeparators.put(8, "#");
        dictionarySeparators.put(9, "[");
        dictionarySeparators.put(10, "]");
        dictionarySeparators.put(1000, "Eofgram");
    }

    private static void printDictionaries() {
        System.out.println("1. Dictionary of terminals");
        dictionaryTerminalsInCommas.entrySet().forEach(t -> System.out.println("Code = " + t.getKey() + ", value = " + t.getValue()));
        dictionaryTerminals.entrySet().forEach(t -> System.out.println("Code = " + t.getKey() + ", value = " + t.getValue()));
        System.out.println("2. Dictionary of nonterminals");
        dictionaryNonTerminals.entrySet().forEach(t -> System.out.println("Code = " + t.getKey() + ", value = " + t.getValue()));
        System.out.println("3. Dictionary of semantics");
        dictionarySemantics.entrySet().forEach(t -> System.out.println("Code = " + t.getKey() + ", value = " + t.getValue()));
        System.out.println("4. Dictionary of service symbols (separators and Eofgram)");
        dictionarySeparators.entrySet().forEach(t -> System.out.println("Code = " + t.getKey() + ", value = " + t.getValue()));
    }

    private static String codeGrammarInternally(String text) {
        String result = text;
        for (Map.Entry<Integer, String> i : dictionaryTerminalsInCommas.entrySet()) {
            result = result.replace("\'" + i.getValue() + "\'", (i.getKey() + " "));
        }
        for (Map.Entry<Integer,String> i : dictionarySemantics.entrySet()) {
            result = result.replace("$" + i.getValue(), (i.getKey()) + " ");
        }
        for (Map.Entry<Integer,String> i : dictionaryNonTerminals.entrySet()) {
            result = result.replace(i.getValue(), i.getKey().toString() + " ");
        }
        for (Map.Entry<Integer,String> i : dictionarySeparators.entrySet()) {
            result = result.replace(i.getValue(),(i.getKey()) + " ");
        }
        for (Map.Entry<Integer, String> i : dictionaryTerminals.entrySet()) {
            result = result.replace(i.getValue(), (i.getKey()) + " ");
        }
        return result;
    }

    private static void cleanDictionaries() {
        dictionarySeparators.clear();
        dictionaryNonTerminals.clear();
        dictionaryTerminalsInCommas.clear();
        dictionarySemantics.clear();
        terminalsCounter = 51;
        nonTerminalsCounter = 11;
        semanticsCounter = 101;
    }

    private static void parseGrammar(char[] lexemes) {
        int counter = 0;
        fillDictionaryOfNonTerminals(lexemes);
        while (counter < lexemes.length) {
            if (lexemes[counter] == '$') {
                counter++;
                StringBuilder semantics = new StringBuilder();
                while (counter < lexemes.length && !checkIfServiceSymbol(lexemes[counter])) {
                    semantics.append(lexemes[counter]);
                    counter++;
                }
                addSemantics(semantics.toString());
            }
            if (counter == lexemes.length) return;
            if (lexemes[counter] == '\'') {
                counter++;
                StringBuilder terminalInCommas = new StringBuilder();
                while (counter < lexemes.length && lexemes[counter] != '\'') {
                    terminalInCommas.append(lexemes[counter]);
                    counter++;
                }
                counter++;
                addTerminal(terminalInCommas.toString());
            }
            if (counter == lexemes.length) return;
            StringBuilder terminalString = new StringBuilder();
            if (lexemes[counter] >= 'a' && lexemes[counter] <= 'z') {
                terminalString.append(lexemes[counter]);
                counter++;
                while (counter < lexemes.length && !checkIfServiceSymbol(lexemes[counter])) {
                    terminalString.append(lexemes[counter]);
                    counter++;
                }
                if (!dictionaryNonTerminals.containsValue(terminalString.toString())) {
                    if (terminalsCounter > 100) {
                        System.out.println("Invalid format of input: number of terminals greater, than possible (>100). ");
                        System.exit(-1);
                    }
                    dictionaryTerminals.put(terminalsCounter, terminalString.toString());
                    terminalsCounter++;
                }
            } else {
                // met nonterminal
                while (counter < lexemes.length && !checkIfServiceSymbol(lexemes[counter])) {
                    counter++;
                }
            }
            if (counter == lexemes.length) return;
            counter++;
        }
    }

    private static void fillDictionaryOfNonTerminals(char[] lexemes) {
        int counter = 1;
        while (counter < lexemes.length) {
            if (counter == 1) {
                StringBuilder expression = new StringBuilder();
                while (lexemes[counter - 1] != ':') {
                    if (lexemes[counter - 1] != ' ') {
                        expression.append(lexemes[counter - 1]);
                    }
                    counter++;
                }
                counter++;
                addNonTerminal(expression.toString());
            }
            if (counter == lexemes.length) return;
            if (lexemes[counter - 1] == '.' && lexemes[counter] == '\n' && lexemes[counter - 1] != '\'') {
                counter++;
                StringBuilder expression = new StringBuilder();
                while (counter < lexemes.length  && lexemes[counter] != ':') {
                    if (lexemes[counter] != ' ') {
                        expression.append(lexemes[counter]);
                    }
                    counter++;
                }
                addNonTerminal(expression.toString());
            }
            if (counter == lexemes.length) return;
            if (lexemes[counter] >= 'A' && lexemes[counter] <= 'Z') {
                StringBuilder expression = new StringBuilder();
                expression.append(lexemes[counter]);
                counter++;
                while (counter < lexemes.length  && !checkIfServiceSymbol(lexemes[counter])) {
                    expression.append(lexemes[counter]);
                    counter++;
                }
            }
            counter++;
        }
    }

    private static void addTerminal(String value) {
        if (!dictionaryTerminalsInCommas.containsValue(value)) {
            if (terminalsCounter > 100) {
                System.out.println("Invalid format of input: number of terminals greater, than possible (>100). ");
                System.exit(-1);
            }
            dictionaryTerminalsInCommas.put(terminalsCounter, value);
            terminalsCounter++;
        }
    }

    private static void addNonTerminal(String value) {
        if (!dictionaryNonTerminals.containsValue(value) && !value.equals("Eofgram")) {
            if (nonTerminalsCounter > 50) {
                System.out.println("Invalid format of input: number of terminals greater, than possible (> 50).");
                System.exit(-1);
            }
            dictionaryNonTerminals.put(nonTerminalsCounter, value);
            nonTerminalsCounter++;
        }
    }

    private static void addSemantics(String value) {
        if (!dictionarySemantics.containsValue(value)) {
            if (semanticsCounter > 150) {
                System.out.println("Invalid format of input: number of terminals greater, than possible (>150). ");
                System.exit(-1);
            }
            dictionarySemantics.put(semanticsCounter, value);
            semanticsCounter++;
        }
    }

    private static boolean checkIfServiceSymbol(char symbol) {
        return (symbol == ',' || symbol == '.' || symbol == ';' ||
                symbol == '*' || symbol == '#' || symbol == '\'' ||
                symbol == '(' || symbol == ')' ||
                symbol == '[' || symbol == ']' || symbol == ' ' || symbol == '\n');
    }
}
