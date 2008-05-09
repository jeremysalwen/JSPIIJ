package benland100.jcalc.input;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import benland100.jcalc.input.tokens.Assigner;
import benland100.jcalc.input.tokens.Constant;
import benland100.jcalc.input.tokens.Function;
import benland100.jcalc.input.tokens.FunctionArgs;
import benland100.jcalc.input.tokens.Grouper;
import benland100.jcalc.input.tokens.Operator;
import benland100.jcalc.input.tokens.StringLiteral;
import benland100.jcalc.input.tokens.Variable;

import pascalTypes.pascalType;
import pascalTypes.types;
import preProcessing.PascalProgram;
import preProcessing.ReturningProcessedObject;
import preProcessing.PreProcessedOnly.PreprocessedFunction;
import preProcessing.returning.functionCall;
import processing.CodeProcessor;

public class Lexer extends ReturningProcessedObject {
	private final static String regex1 = "\\d+\\.?\\d*|[a-zA-Z][a-zA-Z|\\d]*|\\>\\>|[\\-\\+\\/\\*\\(\\)\\^\\!\\>\\<\\=\\#\\%]|\"[^\"]*\"|\\[.*\\]|\\{.*\\}";

	private final static String regex2 = "\\d+\\.?\\d*";

	private final static String regex3 = "[a-zA-Z][a-zA-Z|\\d]*";

	private final static String regex4 = "\\(|\\)|\\[|\\]";

	private final static String regex5 = "\\+|\\-|\\*|\\/|\\^|\\!|\\>|\\<|\\=|\\#|\\%";

	private final static String regex6 = "[a-zA-Z][a-zA-Z|\\d]*";

	private final static String regex7 = "\".*\"";

	private final static String regex8 = ">{2}";

	private final static String regex9 = "\\[.*\\]";

	private final static Pattern tokenizer = Pattern.compile(regex1);

	private final static Pattern constant = Pattern.compile(regex2);

	private final static Pattern function = Pattern.compile(regex3);

	private final static Pattern grouper = Pattern.compile(regex4);

	private final static Pattern operator = Pattern.compile(regex5);

	private final static Pattern variable = Pattern.compile(regex6);

	private final static Pattern string = Pattern.compile(regex7);

	private final static Pattern assigner = Pattern.compile(regex8);

	private final static Pattern functionArgs = Pattern.compile(regex9);

	PreprocessedFunction p;

	private Token[] tokens;

	public Lexer(String data, PreprocessedFunction p) {
		this.p = p;
		Matcher m = tokenizer.matcher(data);
		ArrayList<Token> tokens = new ArrayList<Token>(0);
		while (m.find()) {
			String dat = m.group();
			Token token;
			boolean nextBrace = false;
			try {
				nextBrace = data.substring(m.end(), m.end() + 1).equals("[");
			} catch (Exception e) {
			}
			if (constant.matcher(dat).matches()) {
				token = new Constant(dat);
			} else if (operator.matcher(dat).matches()) {
				token = new Operator(dat);
			} else if (grouper.matcher(dat).matches()) {
				token = new Grouper(dat);
			} else if (function.matcher(dat).matches() && nextBrace) {
				token = new functionCall(dat, );
			} else if (variable.matcher(dat).matches()) {
				try {
					token = new Variable(dat, p);
				} catch (RuntimeException e) {
					token = new functionCall(dat, p);
				}
			} else if (string.matcher(dat).matches()) {
				token = new StringLiteral(dat);
			} else if (assigner.matcher(dat).matches()) {
				token = new Assigner(dat);
			} else if (functionArgs.matcher(dat).matches()) {
				token = new FunctionArgs(dat, p);
			} else {
				throw new RuntimeException("Invalid Token: " + dat);
			}
			tokens.add(token);
		}
		this.tokens = tokens.toArray(new Token[0]);
	}

	public Token tokens(int i) {
		return tokens[i];
	}

	public Token[] tokens() {
		return tokens;
	}

	public int totalTokens() {
		return tokens.length;
	}

	@Override
	public types getReturnType(PreprocessedFunction p) {
		return types.Double;
	}

	@Override
	public pascalType getValue(CodeProcessor c) {
		return new pascalTypes.Double(new Parser(this.tokens(), c).evaluate());
	}
}
