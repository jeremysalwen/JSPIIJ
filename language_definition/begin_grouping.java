package language_definition;

import java.util.Stack;

import tokens.grouping.grouper_token;

public class begin_grouping extends pre_token {
	Class<? extends grouper_token> classref;

	@Override
	public void add_to_token_stream(Stack<grouper_token> grouper_stack) {
		try {
			grouper_token t = classref.newInstance();
			grouper_stack.peek().put(t);
			grouper_stack.push(t);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
