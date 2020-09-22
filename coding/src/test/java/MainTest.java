import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void codeGrammar1() {
        String grammar = "expression : ( $formula )*( ',' ) . Eofgram";
        Main main1 = new Main();
        String result = main1.codeGrammar(grammar);
        Assert.assertEquals("11 1 2 101 3 5 2 51 3 4 1000 ", result);
    }

    @Test
    public void codeGrammar2() {
        String grammar = "formula : ( *( $opcode1 'operation' ) $operand )*(" +
                "      ( $opcode2 'assignation' ; $opcode2 'operation' ) ) . Eofgram";
        Main main2 = new Main();
        String result = main2.codeGrammar(grammar);
        Assert.assertEquals("11 1 2 5 2 101 51 3 102 3 5 2 2 103 52 6 103 51 3 3 4 1000 ", result);
    }

    @Test
    public void codeGrammar3() {
        String grammar = "formula : ( *( $opcode1 'operation' ) $operand )*(" +
                "      ( $opcode2 'assignation' ; $opcode2 'operation' ) ) . Eofgram";
        Main main2 = new Main();
        String result = main2.codeGrammar(grammar);
        Assert.assertEquals("11 1 2 5 2 101 51 3 102 3 5 2 2 103 52 6 103 51 3 3 4 1000 ", result);
    }

    @Test
    public void codeGrammar4() {
        String grammar = "expression : ( formula )*( abcde ) ." +
                "formula : ( *( $opcode1 '<operation>' ) $operand run )*(" +
                "      ( $opcode2 '<assignation>' ; $opcode2 '<operation>' ) ) . Eofgram";
        Main main2 = new Main();
        String result = main2.codeGrammar(grammar);
        Assert.assertEquals("11 1 2 12 3 5 2 51 3 4 12 1 2 5 2 101 52 3 102 53 3 5 2 2 103 54 6 103 52 3 3 4 1000 ", result);
    }

    @Test
    public void codeGrammar5() {
        String grammar = "expression : ( formula )*( ',' ) ." +
                "formula : ( *( $opcode1 '<operation>' ) operand $operand )*(" +
                "      ( $opcode2 '<assignation>' ; $opcode2 '<operation>' ) ) ." +
                "operand : ( $incr1 '<increment>' $tag '<tag>' )." +
                "Eofgram";
        Main main2 = new Main();
        String result = main2.codeGrammar(grammar);
        Assert.assertEquals("11 1 2 12 3 5 2 51 3 4 12 1 2 5 2 101 52 3 13 102 3 5 2 2 103 53 6 103 52 3 3 4 13 1 2 104 54 105 55 3 4 1000 ", result);
    }

}
