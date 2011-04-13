package com.js.interpreter.tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import com.js.interpreter.exceptions.GroupingException;
import com.js.interpreter.startup.FileScriptSource;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.Token;

public class LexerTest {

	/**
	 * @param args
	 * @throws IOException
	 * @throws GroupingException
	 */
	public static void main(String[] args) throws IOException,
			GroupingException {
		String text = "program pascal;\n var i: integer; b,z:string; begin i:=5; for j:=1 to 10 do begin f(x,y,z); call; end; end.";
		Lexer l = new Lexer(new StringReader(text), "lol",
				new ArrayList<ScriptSource>() {
					{
						add(new FileScriptSource(
								"/home/jeremy/java/pascalinterpreterinjava/"));
					}
				});
		Grouper g = new Grouper(new StringReader(text), "wtf",
				new ArrayList<ScriptSource>() {
					{
						add(new FileScriptSource(
								"/home/jeremy/java/pascalinterpreterinjava/"));
					}
				});
		Token t;
		l.yylex();
		g.parse();
		System.out.println(l.token_queue);
		System.out.println(g.token_queue);
	}
}
