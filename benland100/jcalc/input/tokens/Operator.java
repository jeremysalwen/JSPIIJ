package benland100.jcalc.input.tokens;

import benland100.jcalc.input.Token;

public class Operator extends Token {
	
	public final static String ADD = "+";
	public final static String SUBTRACT = "-";
	public final static String MUTIPLY = "*";
	public final static String DIVIDE = "/";
	public final static String EXPONENT = "^";
	public final static String FRACTORIAL = "!";
	
	private String type;
	
	public Operator(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public String toString() {
		return type;
	}
}
