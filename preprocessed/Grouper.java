package preprocessed;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Stack;

import tokens.assignment_token;
import tokens.base_grouper_token;
import tokens.begin_end_token;
import tokens.colon_token;
import tokens.do_token;
import tokens.double_token;
import tokens.grouper_token;
import tokens.if_token;
import tokens.integer_token;
import tokens.operator_token;
import tokens.parenthesized_token;
import tokens.period_token;
import tokens.semicolon_token;
import tokens.string_token;
import tokens.then_token;
import tokens.token;
import tokens.operator_types;
import tokens.while_token;
import tokens.word_token;
import exceptions.grouping_exception;

public class Grouper {
	public LinkedList<token> tokens;

	public Grouper(String text) throws grouping_exception {
		StringReader reader = new StringReader(text);
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.slashSlashComments(true);
		tokenizer.slashStarComments(true);
		tokenizer.ordinaryChar('\"');
		boolean leave_do_loop = false;
		operator_types temp_type = null;
		Stack<grouper_token> stack_of_groupers = new Stack<grouper_token>();
		stack_of_groupers.add(new base_grouper_token());
		try {
			do {
				grouper_token top_of_stack = stack_of_groupers.peek();
				token next_token = null;
				switch (tokenizer.nextToken()) {
				case StreamTokenizer.TT_EOF:
					leave_do_loop = true;
					break;
				case StreamTokenizer.TT_WORD:
					if (tokenizer.sval.equals("begin")) {
						next_token = new begin_end_token();
					} else if (tokenizer.sval.equals("end")) {
						if (!(stack_of_groupers.pop() instanceof begin_end_token)) {
							throw new grouping_exception(
									grouping_exception.grouping_exception_types.MISMATCHED_PARENS);
						} else if (stack_of_groupers.size() == 0) {
							throw new grouping_exception(
									grouping_exception.grouping_exception_types.EXTRA_END);
						}
						stack_of_groupers.peek().add_token(top_of_stack);
					} else if (tokenizer.sval.equals("if")) {
						next_token = new if_token();
					} else if (tokenizer.sval.equals("then")) {
						next_token = new then_token();
					} else if (tokenizer.sval.equals("while")) {
						next_token = new while_token();
					} else if (tokenizer.sval.equals("do")) {
						next_token = new do_token();
					} else if (tokenizer.sval.equals("and")) {
						temp_type = operator_types.AND;
					} else if (tokenizer.sval.equals("or")) {
						temp_type = operator_types.OR;
					} else if (tokenizer.sval.equals("xor")) {
						temp_type = operator_types.XOR;
					} else if (tokenizer.sval.equals("shl")) {
						temp_type = operator_types.SHIFTLEFT;
					} else if (tokenizer.sval.equals("shr")) {
						temp_type = operator_types.SHIFTRIGHT;
					} else if (tokenizer.sval.equals("div")) {
						temp_type = operator_types.DIV;
					} else if (tokenizer.sval.equals("mod")) {
						temp_type = operator_types.MOD;
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
				case '\'':
					next_token = new string_token(tokenizer.sval);
				case '(':
					next_token = new parenthesized_token();
				case ')':
					if (!(stack_of_groupers.pop() instanceof parenthesized_token)) {
						throw new grouping_exception(
								grouping_exception.grouping_exception_types.MISMATCHED_BEGIN_END);
					} else if (stack_of_groupers.size() == 0) {
						throw new grouping_exception(
								grouping_exception.grouping_exception_types.EXTRA_END_PARENS);
					}
					stack_of_groupers.peek().add_token(top_of_stack);
					break;
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
				}
				if (temp_type != null) {
					next_token = new operator_token(temp_type);
					temp_type = null;
				}
				top_of_stack.add_token(next_token);
			} while (!leave_do_loop);
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
}
