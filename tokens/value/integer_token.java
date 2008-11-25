package tokens.value;


public class integer_token implements value_token {
	public int value;

	public integer_token(int i) {
		value = i;
	}

	@Override
	public String toString() {
		return "integer_value_of[" + value + ']';
	}
}
