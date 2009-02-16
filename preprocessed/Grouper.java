package preprocessed;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Stack;

import tokens.EOF_token;
import tokens.token;
import tokens.basic.assignment_token;
import tokens.basic.case_token;
import tokens.basic.colon_token;
import tokens.basic.comma_token;
import tokens.basic.do_token;
import tokens.basic.downto_token;
import tokens.basic.else_token;
import tokens.basic.for_token;
import tokens.basic.function_token;
import tokens.basic.if_token;
import tokens.basic.of_token;
import tokens.basic.period_token;
import tokens.basic.procedure_token;
import tokens.basic.program_token;
import tokens.basic.repeat_token;
import tokens.basic.semicolon_token;
import tokens.basic.then_token;
import tokens.basic.to_token;
import tokens.basic.until_token;
import tokens.basic.var_token;
import tokens.basic.while_token;
import tokens.grouping.base_grouper_token;
import tokens.grouping.begin_end_token;
import tokens.grouping.grouper_token;
import tokens.grouping.parenthesized_token;
import tokens.grouping.record_token;
import tokens.grouping.type_token;
import tokens.value.double_token;
import tokens.value.integer_token;
import tokens.value.operator_token;
import tokens.value.operator_types;
import tokens.value.string_token;
import tokens.value.word_token;
import exceptions.grouping_exception;

public class Grouper implements Runnable {
	public base_grouper_token token_queue;

	private StreamTokenizer tokenizer;

	private Stack<grouper_token> stack_of_groupers;

	public Grouper(String text) throws grouping_exception {
		token_queue = new base_grouper_token();
		StringReader reader = new StringReader(text);
		tokenizer = new StreamTokenizer(reader);
		// tokenizer.slashSlashComments(true);
		tokenizer.slashStarComments(true);
		tokenizer.ordinaryChar('\"');
		tokenizer.ordinaryChar('.');
		tokenizer.eolIsSignificant(false);
		tokenizer.lowerCaseMode(true);
		stack_of_groupers = new Stack<grouper_token>();
	}

	public void run() {
		operator_types temp_type = null;
		stack_of_groupers.add(token_queue);
		try {
			grouper_token top_of_stack;
			do_loop_break: do {
				top_of_stack = stack_of_groupers.peek();
				token next_token = null;
				switch (tokenizer.nextToken()) {
				case StreamTokenizer.TT_EOF:
					top_of_stack.put(new EOF_token());
					break do_loop_break;
				case StreamTokenizer.TT_WORD:
					tokenizer.sval = tokenizer.sval.intern();
					if (tokenizer.sval == "begin") {
						begin_end_token tmp = new begin_end_token();
						top_of_stack.put(tmp);
						stack_of_groupers.push(tmp);
						continue do_loop_break;
					} else if (tokenizer.sval == "record") {
						record_token tmp = new record_token();
						top_of_stack.put(tmp);
						stack_of_groupers.push(tmp);
						continue do_loop_break;
					} else if (tokenizer.sval == "end") {
						if (stack_of_groupers.size() > 0
								&& stack_of_groupers.peek() instanceof begin_end_token
								|| stack_of_groupers.peek() instanceof record_token) {
							top_of_stack.put(new EOF_token());
							stack_of_groupers.pop();
							continue do_loop_break;
						}
						System.err.println("Extra 'end' token encountered");
					} else if (tokenizer.sval == "if") {
						next_token = new if_token();
					} else if (tokenizer.sval == "then") {
						next_token = new then_token();
					} else if (tokenizer.sval == "while") {
						next_token = new while_token();
					} else if (tokenizer.sval == "do") {
						next_token = new do_token();
					} else if (tokenizer.sval == "and") {
						temp_type = operator_types.AND;
					} else if (tokenizer.sval == "not") {
						temp_type = operator_types.NOT;
					} else if (tokenizer.sval == "or") {
						temp_type = operator_types.OR;
					} else if (tokenizer.sval == "var") {
						next_token = new var_token();
					} else if (tokenizer.sval == "type") {
						next_token = new type_token();
					} else if (tokenizer.sval == "xor") {
						temp_type = operator_types.XOR;
					} else if (tokenizer.sval == "shl") {
						temp_type = operator_types.SHIFTLEFT;
					} else if (tokenizer.sval == "shr") {
						temp_type = operator_types.SHIFTRIGHT;
					} else if (tokenizer.sval == "div") {
						temp_type = operator_types.DIV;
					} else if (tokenizer.sval == "mod") {
						temp_type = operator_types.MOD;
					} else if (tokenizer.sval == "procedure") {
						next_token = new procedure_token();
					} else if (tokenizer.sval == "function") {
						next_token = new function_token();
					} else if (tokenizer.sval == "program") {
						next_token = new program_token();
					} else if (tokenizer.sval == "else") {
						next_token = new else_token();
					} else if (tokenizer.sval == "for") {
						next_token = new for_token();
					} else if (tokenizer.sval == "to") {
						next_token = new to_token();
					} else if (tokenizer.sval == "downto") {
						next_token = new downto_token();
					} else if (tokenizer.sval == "repeat") {
						next_token = new repeat_token();
					} else if (tokenizer.sval == "until") {
						next_token = new until_token();
					} else if (tokenizer.sval == "case") {
						next_token = new case_token();
					} else if (tokenizer.sval == "of") {
						next_token = new of_token();
					} else {
						next_token = new word_token(tokenizer.sval);
					}
					break;
				case StreamTokenizer.TT_NUMBER:
					if (((int) tokenizer.nval) == tokenizer.nval) {
						next_token = new integer_token((int) tokenizer.nval);
					} else {
						next_token = new double_token(tokenizer.nval);
					}
					break;
				case ';':
					next_token = new semicolon_token();
					break;
				case '.':
					next_token = new period_token();
					break;
				case '\'':
					next_token = new string_token(tokenizer.sval);
					break;
				case '(':
					parenthesized_token p_token = new parenthesized_token();
					top_of_stack.put(p_token);
					stack_of_groupers.push(p_token);
					continue do_loop_break;
				case ')':
					if (!(stack_of_groupers.pop() instanceof parenthesized_token)) {
						throw new grouping_exception(
								grouping_exception.grouping_exception_types.MISMATCHED_BEGIN_END);
					} else if (stack_of_groupers.size() == 0) {
						throw new grouping_exception(
								grouping_exception.grouping_exception_types.EXTRA_END_PARENS);
					}
					top_of_stack.put(new EOF_token());
					continue do_loop_break;
				case '=':
					temp_type = operator_types.EQUALS;
					break;
				case ':':
					int next = tokenizer.nextToken();
					if (next == '=') {
						next_token = new assignment_token();
					} else {
						tokenizer.pushBack();
						next_token = new colon_token();
					}
					break;
				case '/':
					temp_type = operator_types.DIVIDE;
					break;
				case '*':
					temp_type = operator_types.MULTIPLY;
					break;
				case '+':
					temp_type = operator_types.PLUS;
					break;
				case '-':
					temp_type = operator_types.MINUS;
					break;
				case '<':
					next = tokenizer.nextToken();
					switch (next) {
					case '>':
						temp_type = operator_types.NOTEQUAL;
						break;
					case '=':
						temp_type = operator_types.LESSEQ;
						break;
					default:
						tokenizer.pushBack();
						temp_type = operator_types.LESSTHAN;
					}
					break;
				case '>':
					if (tokenizer.nextToken() == '=') {
						temp_type = operator_types.GREATEREQ;
					} else {
						tokenizer.pushBack();
						temp_type = operator_types.GREATERTHAN;
					}
					break;
				case ',':
					next_token = new comma_token();
				}
				if (temp_type != null) {
					next_token = new operator_token(temp_type);
					temp_type = null;
				}
				if (next_token != null) {
					top_of_stack.put(next_token);
				}
			} while (true);
		} catch (IOException e) {
		}
		if (stack_of_groupers.size() != 1) {
			if (stack_of_groupers.peek() instanceof parenthesized_token) {
				throw new grouping_exception(
						grouping_exception.grouping_exception_types.UNFINISHED_PARENS);
			} else if (stack_of_groupers.peek() instanceof begin_end_token) {
				throw new grouping_exception(
						grouping_exception.grouping_exception_types.UNFINISHED_BEGIN_END);
			} else {
				throw new grouping_exception(null);
			}
		}
	}
}
