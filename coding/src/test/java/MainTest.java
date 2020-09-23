import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void codeGrammar1() {
        String grammar = "expression : ( $formula )*( ',' ) . Eofgram";
        Main main1 = new Main();
        String result = main1.codeGrammar(grammar);
        Assert.assertEquals("11  1  2  101  3 5 2  51  3  4  1000 ", result);
    }

    @Test
    public void codeGrammar2() {
        String grammar = "formula : ( *( $opcode1 'operation' ) $operand )*( ( $opcode2 'assignation' ; $opcode2 'operation' ) ) . Eofgram";
        Main main2 = new Main();
        String result = main2.codeGrammar(grammar);
        Assert.assertEquals("11  1  2  5 2  101  51  3  102  3 5 2  2  103  52  6  103  51  3  3  4  1000 ", result);
    }

    @Test
    public void codeGrammar3() {
        String grammar = "formula : ( *( $opcode1 'operation' ) $operand )*(" +
                "      ( $opcode2 'assignation' ; $opcode2 'operation' ) ) . Eofgram";
        Main main2 = new Main();
        String result = main2.codeGrammar(grammar);
        Assert.assertEquals("11  1  2  5 2  101  51  3  102  3 5 2       2  103  52  6  103  51  3  3  4  1000 ", result);
    }

    @Test
    public void codeGrammar4() {
        String grammar = "expression : ( formula )*( abcde ) .\n" +
                "formula : ( *( $opcode1 '<operation>' ) $operand run )*(( $opcode2 '<assignation>' ; $opcode2 '<operation>' ) ) .\n "+
                "Eofgram";
        Main main2 = new Main();
        String result = main2.codeGrammar(grammar);
        Assert.assertEquals("11  1  2  12  3 5 2  51  3  4 \n" +
                "12  1  2  5 2  101  52  3  102  53  3 5 2 2  103  54  6  103  52  3  3  4 \n" +
                " 1000 ", result);
    }

    @Test
    public void codeGrammar5() {
        String grammar = "expression : ( formula )*( ',' ) .\n" +
                "formula : ( *( $opcode1 '<operation>' ) operand $operand )*(\n" +
                "      ( $opcode2 '<assignation>' ; $opcode2 '<operation>' ) ) .\n" +
                "operand : ( (  $incr1 '<increment>' $tag '<tag>' ;\n" +
                "          $tag '<tag>' ( $incr2 '<increment>' ;\n" +
                "                         ( $array1 '[' formula $array2 ']' )* ;\n" +
                "                         $call1 '(' [ expression $call2 ]\n" +
                "                                 $call3 ')' ;\n" +
                "                         $ident ) ;\n" +
                "          $op1 '(' expression $op2 ')' )\n" +
                "               [ ( $dot '.' ; $arrow '->' ) $field '<tag>' ] ;\n" +
                "    $tag '<label>' ;\n" +
                "    $number '<number>' ;  $char '<char>' ; $string '<string>'\n" +
                "           )*( $cond1 '?' expression $cond2 ':' ) .\n" +
                "Eofgram";
        Main main2 = new Main();
        String result = main2.codeGrammar(grammar);
        Assert.assertEquals("11  1  2  12  3 5 2  51  3  4 \n" +
                "12  1  2  5 2  101  52  3  13  102  3 5 2 \n" +
                "      2  103  53  6  103  52  3  3  4 \n" +
                "13  1  2  2   104  54  105  55  6 \n" +
                "          105  55  2  106  54  6 \n" +
                "                         2  107  56  12  108  57  3 5  6 \n" +
                "                         109  58  9  11  110  10 \n" +
                "                                 111  59  6 \n" +
                "                         112  3  6 \n" +
                "          113  58  11  114  59  3 \n" +
                "               9  2  115  60  6  116  61  3  117  55  10  6 \n" +
                "    105  62  6 \n" +
                "    118  63  6   119  64  6  120  65 \n" +
                "           3 5 2  121  66  11  122  67  3  4 \n" +
                "1000 ", result);
    }

}
