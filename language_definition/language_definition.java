package language_definition;

import java.util.ArrayList;
import java.util.HashMap;

public class language_definition {
	static ArrayList<language_definition> language_definitions;

	HashMap<String, pre_token> token_definitions;

	pre_token semicolon;

	pre_token period;

	pre_token single_quote;

	pre_token open_paren;

	pre_token close_paren;

	pre_token equals;

	pre_token colon;

	pre_token forward_slash;

	pre_token asterix;

	pre_token plus;

	pre_token minus;

	pre_token less_than;

	pre_token greater_than;

	pre_token comma;

}
