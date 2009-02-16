package language_definition;

import java.util.Stack;

import tokens.grouping.grouper_token;

public abstract class pre_token {
	public abstract void add_to_token_stream(Stack<grouper_token> grouper_stack);
}
