package tokens;

public class operator_token implements token {
	public static enum types {
		MULTIPLY, DIVIDE, PLUS, MINUS, NOT, AND, OR, XOR, SHIFTLEFT, SHIFTRIGHT, LESSTHAN, GREATERTHAN, EQUALS, LESSEQ, GREATEREQ, NOTEQUAL, DIV, MOD,PERIOD
	};

	types type;

	public operator_token(types t) {
		this.type = t;
	}
}
