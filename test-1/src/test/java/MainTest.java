import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void printAlphabeticalIndex1() {
        Map<Integer, String[]> text = new HashMap<>();

        String[] string1 = {"Hello", "world"};
        text.put(1, string1);
        assertTrue(Main.generateAlphabeticalIndex(text).containsKey("hello"));

        String[] string2 = {"it", "was", "the", "best", "of", "times"};
        text.put(2, string2);
        assertTrue(Main.generateAlphabeticalIndex(text).containsKey("times"));

        String[] string3 = {"it", "was", "the", "worst", "of", "times"};
        text.put(3, string3);
        assertTrue(Main.generateAlphabeticalIndex(text).containsKey("worst"));
        System.out.println(Main.printAlphabeticalIndex(Main.generateAlphabeticalIndex(text)));
        assertEquals("best 2 \n" +
                "hello 1 \n" +
                "it 2 3 \n" +
                "of 2 3 \n" +
                "the 2 3 \n" +
                "times 2 3 \n" +
                "was 2 3 \n" +
                "world 1 \n" +
                "worst 3 \n", Main.printAlphabeticalIndex(Main.generateAlphabeticalIndex(text)));
    }

    @Test
    public void printAlphabeticalIndex2() {
        Map<Integer, String[]> text = new HashMap<>();

        String[] string1 = {"abc", "abc", "cde"};
        text.put(1, string1);
        assertTrue(Main.generateAlphabeticalIndex(text).containsKey("abc"));

        String[] string2 = {"def", "fgh", "cde"};
        text.put(2, string2);
        assertTrue(Main.generateAlphabeticalIndex(text).containsKey("fgh"));

        String[] string3 = {"abc", "cdd"};
        text.put(3, string3);

        String[] string4 = {"def", "cdd", "ggg"};
        text.put(4, string4);
        assertTrue(Main.generateAlphabeticalIndex(text).containsKey("ggg"));

        System.out.println(Main.printAlphabeticalIndex(Main.generateAlphabeticalIndex(text)));
        assertEquals("abc 1 3 \n" +
                "cdd 3 4 \n" +
                "cde 1 2 \n" +
                "def 2 4 \n" +
                "fgh 2 \n" +
                "ggg 4 \n", Main.printAlphabeticalIndex(Main.generateAlphabeticalIndex(text)));
    }

    @Test
    public void printAlphabeticalIndex3() {
        Map<Integer, String[]> text = new HashMap<>();

        String[] string1 = {"maybe", "i", "maybe", "you", "tuta", "perennat"};
        text.put(1, string1);

        String[] string2 = {"hic", "tuta", "perennat"};
        text.put(2, string2);

        String[] string3 = {"in", "vino", "veritas"};
        text.put(3, string3);

        String[] string4 = {"finita", "la", "comedia", "in", "vino"};
        text.put(4, string4);

        String[] string5 = {"finita", "la", "comedia", "tuta", "perennat"};
        text.put(5, string5);

        System.out.println(Main.printAlphabeticalIndex(Main.generateAlphabeticalIndex(text)));
        assertEquals("comedia 4 5 \n" +
                "finita 4 5 \n" +
                "hic 2 \n" +
                "i 1 \n" +
                "in 3 4 \n" +
                "la 4 5 \n" +
                "maybe 1 \n" +
                "perennat 1 2 5 \n" +
                "tuta 1 2 5 \n" +
                "veritas 3 \n" +
                "vino 3 4 \n" +
                "you 1 \n", Main.printAlphabeticalIndex(Main.generateAlphabeticalIndex(text)));
    }
}
