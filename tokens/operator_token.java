package tokens;

public class operator_token implements token {
	public operator_types type;

	public operator_token(operator_types t) {
		this.type = t;
	}

	public boolean can_be_unary() {
		switch (type) {
		case MINUS:
		case NOT:
		case PLUS:
			return true;
		default:
			return false;
		}
	}
}
