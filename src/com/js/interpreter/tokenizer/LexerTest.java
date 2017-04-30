package com.js.interpreter.tokenizer;

import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.startup.FileScriptSource;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokens.Token;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LexerTest {

    /**
     * @param args
     * @throws IOException
     * @throws GroupingException
     */
    @Deprecated
    public static void main(String[] args) throws IOException,
            GroupingException {
        String file = "scripts/concord.pas";
        String text = "program pascal;\n var i: integer; b,z:string; begin i:=5; for j:=1 to 10 do begin f(x,y,z); call; end; end.";
        NewLexer l = new NewLexer(new FileReader(file), "lol",
                new ArrayList<ScriptSource>() {
                    {
                        add(new FileScriptSource(
                                "/home/jeremy/java/pascalinterpreterinjava/"));
                    }
                });
        Grouper g = new Grouper(new FileReader(file), "wtf",
                new ArrayList<ScriptSource>() {
                    {
                        add(new FileScriptSource(
                                "/home/jeremy/java/pascalinterpreterinjava/"));
                    }
                });
        Token t;
        l.parse();
        g.parse();
        System.out.println(l.token_queue);
        System.out.println(g.token_queue);
    }
}
