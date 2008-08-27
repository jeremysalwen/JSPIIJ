package preprocessed;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import tokens.EOF_token;
import tokens.assignment_token;
import tokens.base_grouper_token;
import tokens.begin_end_token;
import tokens.colon_token;
import tokens.comma_token;
import tokens.do_token;
import tokens.double_token;
import tokens.end_token;
import tokens.function_token;
import tokens.grouper_token;
import tokens.if_token;
import tokens.integer_token;
import tokens.operator_token;
import tokens.operator_types;
import tokens.parenthesized_token;
import tokens.period_token;
import tokens.procedure_token;
import tokens.record_token;
import tokens.semicolon_token;
import tokens.string_token;
import tokens.then_token;
import tokens.token;
import tokens.type_token;
import tokens.var_token;
import tokens.while_token;
import tokens.word_token;
import exceptions.grouping_exception;

public class Grouper {
	public Queue<token> tokens;

	public Grouper(String text) throws grouping_exception {
		StringReader reader = new StringReader(text);
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		// tokenizer.slashSlashComments(true);
		tokenizer.slashStarComments(true);
		tokenizer.ordinaryChar('\"');
		operator_types temp_type = null;
		Stack<grouper_token> stack_of_groupers = new Stack<grouper_token>();
		stack_of_groupers.add(new base_grouper_token());
		try {
			grouper_token top_of_stack;
			do_loop_break: do {
				top_of_stack = stack_of_groupers.peek();
				token next_token = null;
				switch (tokenizer.nextToken()) {
				case StreamTokenizer.TT_EOF:
					top_of_stack.add_token(new EOF_token());
					break do_loop_break;
				case StreamTokenizer.TT_WORD:
					tokenizer.sval = tokenizer.sval.intern();
					if (tokenizer.sval == "begin") {
						stack_of_groupers.push(new begin_end_token());
						continue do_loop_break;
					} else if (tokenizer.sval == "end") {
						if (stack_of_groupers.size() > 0
								&& stack_of_groupers.peek() instanceof begin_end_token) {
							stack_of_groupers.pop();
							top_of_stack.add_token(new EOF_token());
							continue do_loop_break;
						}
						next_token = new end_token();
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
					} else if (tokenizer.sval == "record") {
						next_token = new record_token();
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
					top_of_stack.add_token(p_token);
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
					top_of_stack.add_token(new EOF_token());
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
				top_of_stack.add_token(next_token);
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
		tokens = ((base_grouper_token) stack_of_groupers.peek()).tokens;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (token t : tokens) {
			s.append(t).append(' ');
		}
		return s.toString();
	}
}
