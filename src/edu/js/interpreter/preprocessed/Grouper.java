package edu.js.interpreter.preprocessed;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Stack;

import edu.js.interpreter.exceptions.GroupingException;
import edu.js.interpreter.tokens.EOF_Token;
import edu.js.interpreter.tokens.Token;
import edu.js.interpreter.tokens.basic.AssignmentToken;
import edu.js.interpreter.tokens.basic.CaseToken;
import edu.js.interpreter.tokens.basic.ColonToken;
import edu.js.interpreter.tokens.basic.CommaToken;
import edu.js.interpreter.tokens.basic.DoToken;
import edu.js.interpreter.tokens.basic.DowntoToken;
import edu.js.interpreter.tokens.basic.ElseToken;
import edu.js.interpreter.tokens.basic.ForToken;
import edu.js.interpreter.tokens.basic.FunctionToken;
import edu.js.interpreter.tokens.basic.IfToken;
import edu.js.interpreter.tokens.basic.OfToken;
import edu.js.interpreter.tokens.basic.PeriodToken;
import edu.js.interpreter.tokens.basic.ProcedureToken;
import edu.js.interpreter.tokens.basic.ProgramToken;
import edu.js.interpreter.tokens.basic.RepeatToken;
import edu.js.interpreter.tokens.basic.SemicolonToken;
import edu.js.interpreter.tokens.basic.ThenToken;
import edu.js.interpreter.tokens.basic.ToToken;
import edu.js.interpreter.tokens.basic.UntilToken;
import edu.js.interpreter.tokens.basic.VarToken;
import edu.js.interpreter.tokens.basic.WhileToken;
import edu.js.interpreter.tokens.grouping.BaseGrouperToken;
import edu.js.interpreter.tokens.grouping.BeginEndToken;
import edu.js.interpreter.tokens.grouping.BracketedToken;
import edu.js.interpreter.tokens.grouping.GrouperToken;
import edu.js.interpreter.tokens.grouping.ParenthesizedToken;
import edu.js.interpreter.tokens.grouping.RecordToken;
import edu.js.interpreter.tokens.grouping.TypeToken;
import edu.js.interpreter.tokens.value.DoubleToken;
import edu.js.interpreter.tokens.value.IntegerToken;
import edu.js.interpreter.tokens.value.OperatorToken;
import edu.js.interpreter.tokens.value.OperatorTypes;
import edu.js.interpreter.tokens.value.StringToken;
import edu.js.interpreter.tokens.value.WordToken;

public class Grouper implements Runnable {
	public BaseGrouperToken token_queue;

	private StreamTokenizer tokenizer;

	private Stack<GrouperToken> stack_of_groupers;

	public Grouper(String text) throws GroupingException {
		token_queue = new BaseGrouperToken();
		StringReader reader = new StringReader(text);
		tokenizer = new StreamTokenizer(reader);
		// tokenizer.slashSlashComments(true);
		tokenizer.slashStarComments(true);
		tokenizer.ordinaryChar('\"');
		tokenizer.ordinaryChar('/');
		tokenizer.ordinaryChar('.');
		tokenizer.ordinaryChar('-');
		tokenizer.ordinaryChar('+');
		tokenizer.eolIsSignificant(false);
		tokenizer.lowerCaseMode(true);
		stack_of_groupers = new Stack<GrouperToken>();
	}

	public void run() {
		OperatorTypes temp_type = null;
		stack_of_groupers.add(token_queue);
		try {
			GrouperToken top_of_stack;
			do_loop_break: do {
				top_of_stack = stack_of_groupers.peek();
				Token next_token = null;
				switch (tokenizer.nextToken()) {
				case StreamTokenizer.TT_EOF:
					top_of_stack.put(new EOF_Token());
					break do_loop_break;
				case StreamTokenizer.TT_WORD:
					tokenizer.sval = tokenizer.sval.intern();
					if (tokenizer.sval == "begin") {
						BeginEndToken tmp = new BeginEndToken();
						top_of_stack.put(tmp);
						stack_of_groupers.push(tmp);
						continue do_loop_break;
					} else if (tokenizer.sval == "record") {
						RecordToken tmp = new RecordToken();
						top_of_stack.put(tmp);
						stack_of_groupers.push(tmp);
						continue do_loop_break;
					} else if (tokenizer.sval == "end") {
						if (stack_of_groupers.size() > 0
								&& stack_of_groupers.peek() instanceof BeginEndToken
								|| stack_of_groupers.peek() instanceof RecordToken) {
							top_of_stack.put(new EOF_Token());
							stack_of_groupers.pop();
							continue do_loop_break;
						}
						System.err.println("Extra 'end' token encountered");
					} else if (tokenizer.sval == "if") {
						next_token = new IfToken();
					} else if (tokenizer.sval == "then") {
						next_token = new ThenToken();
					} else if (tokenizer.sval == "while") {
						next_token = new WhileToken();
					} else if (tokenizer.sval == "do") {
						next_token = new DoToken();
					} else if (tokenizer.sval == "and") {
						temp_type = OperatorTypes.AND;
					} else if (tokenizer.sval == "not") {
						temp_type = OperatorTypes.NOT;
					} else if (tokenizer.sval == "or") {
						temp_type = OperatorTypes.OR;
					} else if (tokenizer.sval == "var") {
						next_token = new VarToken();
					} else if (tokenizer.sval == "type") {
						next_token = new TypeToken();
					} else if (tokenizer.sval == "xor") {
						temp_type = OperatorTypes.XOR;
					} else if (tokenizer.sval == "shl") {
						temp_type = OperatorTypes.SHIFTLEFT;
					} else if (tokenizer.sval == "shr") {
						temp_type = OperatorTypes.SHIFTRIGHT;
					} else if (tokenizer.sval == "div") {
						temp_type = OperatorTypes.DIV;
					} else if (tokenizer.sval == "mod") {
						temp_type = OperatorTypes.MOD;
					} else if (tokenizer.sval == "procedure") {
						next_token = new ProcedureToken();
					} else if (tokenizer.sval == "function") {
						next_token = new FunctionToken();
					} else if (tokenizer.sval == "program") {
						next_token = new ProgramToken();
					} else if (tokenizer.sval == "else") {
						next_token = new ElseToken();
					} else if (tokenizer.sval == "for") {
						next_token = new ForToken();
					} else if (tokenizer.sval == "to") {
						next_token = new ToToken();
					} else if (tokenizer.sval == "downto") {
						next_token = new DowntoToken();
					} else if (tokenizer.sval == "repeat") {
						next_token = new RepeatToken();
					} else if (tokenizer.sval == "until") {
						next_token = new UntilToken();
					} else if (tokenizer.sval == "case") {
						next_token = new CaseToken();
					} else if (tokenizer.sval == "of") {
						next_token = new OfToken();
					} else {
						next_token = new WordToken(tokenizer.sval);
					}
					break;
				case StreamTokenizer.TT_NUMBER:
					if (((int) tokenizer.nval) == tokenizer.nval) {
						next_token = new IntegerToken((int) tokenizer.nval);
					} else {
						next_token = new DoubleToken(tokenizer.nval);
					}
					break;
				case ';':
					next_token = new SemicolonToken();
					break;
				case '.':
					next_token = new PeriodToken();
					break;
				case '\'':
					next_token = new StringToken(tokenizer.sval);
					break;
				case '(':
					ParenthesizedToken p_token = new ParenthesizedToken();
					top_of_stack.put(p_token);
					stack_of_groupers.push(p_token);
					continue do_loop_break;
				case ')':
					if (!(stack_of_groupers.pop() instanceof ParenthesizedToken)) {
						throw new GroupingException(
								GroupingException.grouping_exception_types.MISMATCHED_BEGIN_END);
					} else if (stack_of_groupers.size() == 0) {
						throw new GroupingException(
								GroupingException.grouping_exception_types.EXTRA_END_PARENS);
					}
					top_of_stack.put(new EOF_Token());
					continue do_loop_break;
				case '=':
					temp_type = OperatorTypes.EQUALS;
					break;
				case ':':
					int next = tokenizer.nextToken();
					if (next == '=') {
						next_token = new AssignmentToken();
					} else {
						tokenizer.pushBack();
						next_token = new ColonToken();
					}
					break;
				case '/':
					temp_type = OperatorTypes.DIVIDE;
					break;
				case '*':
					temp_type = OperatorTypes.MULTIPLY;
					break;
				case '+':
					temp_type = OperatorTypes.PLUS;
					break;
				case '-':
					temp_type = OperatorTypes.MINUS;
					break;
				case '<':
					next = tokenizer.nextToken();
					switch (next) {
					case '>':
						temp_type = OperatorTypes.NOTEQUAL;
						break;
					case '=':
						temp_type = OperatorTypes.LESSEQ;
						break;
					default:
						tokenizer.pushBack();
						temp_type = OperatorTypes.LESSTHAN;
					}
					break;
				case '>':
					if (tokenizer.nextToken() == '=') {
						temp_type = OperatorTypes.GREATEREQ;
					} else {
						tokenizer.pushBack();
						temp_type = OperatorTypes.GREATERTHAN;
					}
					break;
				case ',':
					next_token = new CommaToken();
				break;
				case '[':
					BracketedToken b_token = new BracketedToken();
					top_of_stack.put(b_token);
					stack_of_groupers.push(b_token);
					continue do_loop_break;
				case ']':
					if (!(stack_of_groupers.pop() instanceof BracketedToken)) {
						throw new GroupingException(
								GroupingException.grouping_exception_types.MISMATCHED_BEGIN_END);
					} else if (stack_of_groupers.size() == 0) {
						throw new GroupingException(
								GroupingException.grouping_exception_types.EXTRA_END_PARENS);
					}
					top_of_stack.put(new EOF_Token());
					continue do_loop_break;
				}
				if (temp_type != null) {
					next_token = new OperatorToken(temp_type);
					temp_type = null;
				}
				if (next_token != null) {
					top_of_stack.put(next_token);
				}
			} while (true);
		} catch (IOException e) {
		}
		if (stack_of_groupers.size() != 1) {
			if (stack_of_groupers.peek() instanceof ParenthesizedToken) {
				throw new GroupingException(
						GroupingException.grouping_exception_types.UNFINISHED_PARENS);
			} else if (stack_of_groupers.peek() instanceof BeginEndToken) {
				throw new GroupingException(
						GroupingException.grouping_exception_types.UNFINISHED_BEGIN_END);
			} else {
				throw new GroupingException(null);
			}
		}
	}
}
