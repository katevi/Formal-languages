import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static int terminalsCounter = 51;
    private static int nonTerminalsCounter = 11;
    private static int semanticsCounter = 101;
    private final static Map<Integer, String> dictionarySeparators = new HashMap<>();
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
            text = fileContent.collect(Collectors.joining());
        } catch (IOException e) {
            System.out.println("File expression.txt not found.");
        }
        String codedGrammar = codeGrammar(text);
        System.out.println("Result of grammar coding:");
        System.out.print(codedGrammar);
    }

    public static String codeGrammar(String text) {
        initializeCodesOfLexemes();
        text = text.replace(" ", "");
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
        for (Map.Entry<Integer, String> i : dictionaryTerminals.entrySet()) {
            result = result.replace("\'" + i.getValue() + "\'", String.valueOf(i.getKey()));
        }
        for (Map.Entry<Integer,String> i : dictionarySemantics.entrySet()) {
            result = result.replace("$" + i.getValue(), String.valueOf(i.getKey()));
        }
        for (Map.Entry<Integer,String> i : dictionaryNonTerminals.entrySet()) {
            result = result.replace(i.getValue(), String.valueOf(i.getKey()));
        }
        for (Map.Entry<Integer,String> i : dictionarySeparators.entrySet()) {
            result = result.replace(i.getValue(), String.valueOf(i.getKey()));
        }
        return result;
    }

    private static void cleanDictionaries() {
        dictionarySeparators.clear();
        dictionaryNonTerminals.clear();
        dictionaryTerminals.clear();
        dictionarySemantics.clear();
        terminalsCounter = 51;
        nonTerminalsCounter = 11;
        semanticsCounter = 101;
    }

    private static void parseGrammar(char[] lexemes) {
        int counter = 1;
        while (counter < lexemes.length) {
            //firstly we always meet an expression
            if (counter == 1) {
                StringBuilder expression = new StringBuilder();
                while (lexemes[counter - 1] != ':') {
                    expression.append(lexemes[counter - 1]);
                    counter++;
                }
                counter++;
                addNonTerminal(expression.toString());
            }
            if (lexemes[counter] == '.') {
                counter++;
                StringBuilder expression = new StringBuilder();
                while (counter < lexemes.length  && lexemes[counter] != ':') {
                    expression.append(lexemes[counter]);
                    counter++;
                }
                addNonTerminal(expression.toString());
            }
            if (counter == lexemes.length) return;
            if (lexemes[counter] == '$') {
                counter++;
                StringBuilder semantics = new StringBuilder();
                while (counter < lexemes.length && !checkIfServiceSymbol(lexemes[counter])) {
                    semantics.append(lexemes[counter]);
                    counter++;
                }
                addSemantics(semantics.toString());
            }
            if (checkIfSymbolTerminal(lexemes, counter)) {
                addTerminal(String.valueOf(lexemes[counter]));
                counter++;
            }
            if (lexemes[counter] == '\'') {
                counter++;
                StringBuilder terminalInCommas = new StringBuilder();
                while (counter < lexemes.length && lexemes[counter] != '\'') {
                    terminalInCommas.append(lexemes[counter]);
                    counter++;
                }
                addTerminal(terminalInCommas.toString());
            }
            counter++;
        }
    }

    private static void addTerminal(String value) {
        if (!dictionaryTerminals.containsValue(value)) {
            dictionaryTerminals.put(terminalsCounter, value);
            terminalsCounter++;
        }
    }

    private static void addNonTerminal(String value) {
        if (!dictionaryNonTerminals.containsValue(value) && !value.equals("Eofgram")) {
            dictionaryNonTerminals.put(nonTerminalsCounter, value);
            nonTerminalsCounter++;
        }
    }

    private static void addSemantics(String value) {
        if (!dictionarySemantics.containsValue(value)) {
            dictionarySemantics.put(semanticsCounter, value);
            semanticsCounter++;
        }
    }

    private static boolean checkIfSymbolTerminal(char[] lexemes, int counter) {
        return (checkIfServiceSymbol(lexemes[counter - 1])
                && checkIfServiceSymbol(lexemes[counter + 1])
                && Character.isLowerCase(lexemes[counter]));
    }

    private static boolean checkIfServiceSymbol(char symbol) {
        return (symbol == ',' || symbol == '.' || symbol == ';' ||
                symbol == '*' || symbol == '#' || symbol == '\'' ||
                symbol == '(' || symbol == ')' ||
                symbol == '[' || symbol == ']');
    }
}
