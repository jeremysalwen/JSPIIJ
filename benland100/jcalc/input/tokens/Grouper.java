package benland100.jcalc.input.tokens;

import benland100.jcalc.input.Token;

public class Grouper extends Token {
	
	public final static String LPAREN = "(";
	public final static String RPAREN = ")";
	public final static String LBRACK = "[";
	public final static String RBRACK = "]";
	
	private String type;
	
	public Grouper(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public String toString() {
		return type;
	}
	
}
