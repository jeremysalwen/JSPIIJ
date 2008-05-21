package tokens;

public class integer_token implements token {
	public int value;

	public integer_token(int i) {
		value = i;
	}

	@Override
	public String toString() {
		return "integer_value_of[" + value + ']';
	}
}
