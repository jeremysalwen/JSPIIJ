package tokens;

public class double_token implements token {
	public double value;

	public double_token(double d) {
		value = d;
	}

	@Override
	public String toString() {
		return "double_of_value[" + value + ']';
	}
}
