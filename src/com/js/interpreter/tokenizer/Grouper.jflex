package com.js.interpreter.tokenizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.List;
import java.util.Stack;

import com.js.interpreter.exceptions.GroupingException;
import com.js.interpreter.exceptions.GroupingException.grouping_exception_types;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokens.*;
import com.js.interpreter.tokens.basic.*;
import com.js.interpreter.tokens.grouping.*;
import com.js.interpreter.tokens.value.*;
/**
 * This class is a simple example lexer.
 */
%%
%unicode
%line
%column
%ignorecase

%class Lexer
%{
	String sourcename;
	public BaseGrouperToken token_queue;
	Stack<GrouperToken> groupers;
	List<ScriptSource> searchDirectories;
	Stack<String> sourcenames;
	
	StringBuilder literal=new StringBuilder();
	
	void TossException(GroupingExceptionToken t) {
		for (GrouperToken g : groupers) {
			g.put(t);
		}
	}
	private String tmpname;
	private Reader tmpreader;
	void addInclude(String name) throws FileNotFoundException {
		for (ScriptSource s : searchDirectories) {
			Reader r = s.read(name);
			if (r != null) {
				this.tmpreader=r;
				this.tmpname=name;
				return;
			}
		}
		throw new FileNotFoundException("Cannot find the $INCLUDE file " + name);
	}
	void commitInclude() {
		sourcenames.push(tmpname);
		yypushStream(tmpreader);
	}
	
	LineInfo getLine() {
		return new LineInfo(yyline,yycolumn,sourcenames.peek());
	}
%}

%ctorarg String sourcename
%ctorarg List<ScriptSource> searchDirectories
%init{
		this.sourcename = sourcename;
		sourcenames=new Stack<String>();
		sourcenames.push(sourcename);
		token_queue = new BaseGrouperToken(getLine());
		groupers = new Stack<GrouperToken>();
		groupers.push(token_queue);
		this.searchDirectories = searchDirectories;
%init}

%type boolean

%eofval{
	if (!yymoreStreams()) {
		Token end = null;
		GrouperToken top_of_stack=groupers.peek();
		if (groupers.size() != 1) {
			if (top_of_stack instanceof ParenthesizedToken) {
				TossException(new GroupingExceptionToken(
					top_of_stack.lineInfo,
					grouping_exception_types.UNFINISHED_PARENS));
			} else if (top_of_stack instanceof BeginEndToken) {
				TossException(new GroupingExceptionToken(
					top_of_stack.lineInfo,
					grouping_exception_types.UNFINISHED_BEGIN_END));
			} else {
				TossException(new GroupingExceptionToken(
					top_of_stack.lineInfo,
					grouping_exception_types.UNFINISHED_CONSTRUCT));
			}
		} else {
			top_of_stack.put(new EOF_Token(getLine()));
		}
		return false;
	}
	sourcenames.pop();
	yypopStream();
%eofval}

Identifier = [a-zA-Z_] [a-zA-Z_0-9]*
Digit = [0-9]
Integer = {Digit}+
Float	= {Digit}+ "." {Digit}+
WhiteSpace = ([ \t] | {LineTerminator})+

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r|\n|]

Comment = {TraditionalComment} | {EndOfLineComment}  | {BracesComment}

CommentStarter		 =  "(*" | "{"
CommentEnder		 =   "*)" | "}"

BracesComment		 = {CommentStarter} {RestOfComment}

RestOfComment		 = ([^*] | \*[^)}])* "}"
 
TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}


IncludeStatement = {CommentStarter}\$(("i" [ \t]) |"include")
CompilerDirective = {CommentStarter}\$ {RestOfComment}

%state STRING
%state STRINGDONE
%state STRINGPOUND
%state INCLUDE
%state INCLUDE_DBL_QUOTE
%state INCLUDE_SNGL_QUOTE
%state END_INCLUDE
%%

<YYINITIAL> {
	{WhiteSpace} {}
	{IncludeStatement} {yybegin(INCLUDE);}
	{CompilerDirective} {System.err.println(getLine() + ": Warning! Unrecognized Compiler Directive!"); }
	
	{Comment} {}


	{Float} {groupers.peek().put(new DoubleToken(getLine(),Double.parseDouble(yytext())));}
	{Integer} {groupers.peek().put(new IntegerToken(getLine(),Integer.parseInt(yytext())));}
	
	"and" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.AND)); }
	"not" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.NOT)); }
	"or" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.OR)); }
	"xor" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.XOR)); }
	"shl" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.SHIFTLEFT)); }
	"shr" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.SHIFTRIGHT)); }
	"div" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.DIV)); }
	"mod" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.MOD)); }
	"=" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.EQUALS)); }
	"/" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.DIVIDE)); }
	"*" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.MULTIPLY)); }
	"+" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.PLUS)); }
	"-" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.MINUS)); }
	"<>" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.NOTEQUAL)); }
	"<=" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.LESSEQ)); }
	">=" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.GREATEREQ)); }
	">" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.GREATERTHAN)); }
	"<" {groupers.peek().put(new OperatorToken(getLine(),OperatorTypes.LESSTHAN)); }
	
	"if" {groupers.peek().put(new IfToken(getLine())); }
	"then" {groupers.peek().put(new ThenToken(getLine())); }
	"while" {groupers.peek().put(new WhileToken(getLine())); }
	"do" {groupers.peek().put(new DoToken(getLine())); }
	"var" {groupers.peek().put(new VarToken(getLine())); }
	"type" {groupers.peek().put(new TypeToken(getLine())); }
	"procedure" {groupers.peek().put(new ProcedureToken(getLine())); }
	"function" {groupers.peek().put(new FunctionToken(getLine())); }
	"program" {groupers.peek().put(new ProgramToken(getLine())); }
	"else" {groupers.peek().put(new ElseToken(getLine())); }
	"for" {groupers.peek().put(new ForToken(getLine())); }
	"to" {groupers.peek().put(new ToToken(getLine())); }
	"downto" {groupers.peek().put(new DowntoToken(getLine())); }
	"repeat" {groupers.peek().put(new RepeatToken(getLine())); }
	"of" {groupers.peek().put(new OfToken(getLine())); }
	"const" {groupers.peek().put(new ConstToken(getLine())); }
	"false" {groupers.peek().put(new BooleanToken(getLine(),false)); }
	"true" {groupers.peek().put(new BooleanToken(getLine(),true)); }
	"forward" {groupers.peek().put(new ForwardToken(getLine())); }
	"array" {groupers.peek().put(new ArrayToken(getLine())); }
	"until" {groupers.peek().put(new UntilToken(getLine())); }
	":=" {groupers.peek().put(new AssignmentToken(getLine())); }
	"," {groupers.peek().put(new CommaToken(getLine())); }
	":" {groupers.peek().put(new ColonToken(getLine())); }
	"." {groupers.peek().put(new PeriodToken(getLine())); }
	";" {groupers.peek().put(new SemicolonToken(getLine())); }
	"begin" {
		BeginEndToken tmp = new BeginEndToken(getLine());
		groupers.peek().put(tmp);
		groupers.push(tmp);
	}
	"record" {
		RecordToken tmp = new RecordToken(getLine());
		groupers.peek().put(tmp);
		groupers.push(tmp);
	}
	"case" {
		CaseToken tmp = new CaseToken(getLine());
		groupers.peek().put(tmp);
		groupers.push(tmp);
	}
	"(" {
		ParenthesizedToken tmp = new ParenthesizedToken(getLine());
		groupers.peek().put(tmp);
		groupers.push(tmp);
	}
	"[" {
		BracketedToken tmp = new BracketedToken(getLine());
		groupers.peek().put(tmp);
		groupers.push(tmp);
	}
	"end" {
			LineInfo line=getLine();
			GrouperToken top_of_stack=groupers.peek();
			if (top_of_stack instanceof BeginEndToken
					|| top_of_stack instanceof RecordToken
					|| top_of_stack instanceof CaseToken) {
				top_of_stack.put(new EOF_Token(getLine()));
				groupers.pop();
			} else {
				TossException(new GroupingExceptionToken(
						line,
						grouping_exception_types.MISMATCHED_BEGIN_END));
				return false;
			}
	}
	")" {
		if (!(groupers.peek() instanceof ParenthesizedToken)) {
			TossException(new GroupingExceptionToken(getLine(),
					grouping_exception_types.MISMATCHED_PARENS));
			return false;
		}
		groupers.pop().put(new EOF_Token(getLine()));
	}
	"]" {
		if (!(groupers.peek() instanceof BracketedToken)) {
			TossException(new GroupingExceptionToken(getLine(),
				grouping_exception_types.MISMATCHED_BRACKETS));
			return false;
		}
		groupers.pop().put(new EOF_Token(getLine()));
	}
	"'" {
		literal.setLength(0);
		yybegin(STRING);
	}
	
	{Identifier} {groupers.peek().put(new WordToken(getLine(),yytext())); }

}
<STRING> {
	"''"	{literal.append('\'');}
	"'"		{yybegin(STRINGDONE);}
	[^'\n\r]* {literal.append(yytext());}
	[\n\r]	{throw new Error("You must close your quotes before starting a new line");}
}
<STRINGPOUND> {
	{Integer} {literal.append((char)Integer.parseInt(yytext())); yybegin(STRINGDONE);}
	.|\n      { throw new Error("Expected character code, not <"+ yytext()+">"); }
	
}
<STRINGDONE> {
	{WhiteSpace} {}
	{Comment} {}
	"'" {yybegin(STRING);}
	"#" {yybegin(STRINGPOUND);}
	.|\n {
			yybegin(YYINITIAL); 
			if(literal.length()==1) {
				groupers.peek().put(new CharacterToken(getLine(),literal.toString().charAt(0)));
			} else {
				groupers.peek().put(new StringToken(getLine(),literal.toString()));
			}
		}
}

<INCLUDE> { 
	{WhiteSpace} {} 
	"'" {literal.setLength(0); yybegin(INCLUDE_SNGL_QUOTE);}
    "\"" {literal.setLength(0); yybegin(INCLUDE_DBL_QUOTE);}
    [^ \r\n*)}]+ {addInclude(yytext()); yybegin(END_INCLUDE);}
    .|\n {throw new Error("Missing file to include");}
}

<INCLUDE_SNGL_QUOTE> {
	"''"	{literal.append('\'');}
	"'"		{addInclude(literal.toString()); yybegin(END_INCLUDE);}
	[^\n\r]+ {literal.append(yytext());}
	[\n\r]	{throw new Error("You must close your quotes before starting a new line");}
}

<INCLUDE_DBL_QUOTE> {
	"\"\""	{literal.append('\"');}
	"\""		{addInclude(literal.toString()); yybegin(END_INCLUDE);}
	[^\n\r]+ {literal.append(yytext());}
	[\n\r]	{throw new Error("You must close your quotes before starting a new line");}
}

<END_INCLUDE> {
	{RestOfComment}	{yybegin(YYINITIAL); commitInclude(); }
	.|\n {TossException(new GroupingExceptionToken(getLine(),
				grouping_exception_types.MISMATCHED_BRACKETS));}
}

/* error fallback */
.|\n                             { throw new Error("Illegal character <"+
                                                    yytext()+">"); }

