package language_definition;

import java.util.Stack;

import tokens.EOF_token;
import tokens.grouping.grouper_token;

public class end_gropuing extends pre_token {
	Class<? extends grouper_token> grouping_type;
	@Override
	public void add_to_token_stream(Stack<grouper_token> grouper_stack) {
		grouper_stack.peek().put(new EOF_token());
		assert (grouping_type.isInstance(grouper_stack.peek()));
		grouper_stack.pop();
	}

}
