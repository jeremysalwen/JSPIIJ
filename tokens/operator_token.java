package tokens;

public class operator_token implements token {
	public operator_types type;

	public operator_token(operator_types t) {
		this.type = t;
	}
}
