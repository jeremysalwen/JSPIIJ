package benland100.jcalc.input.tokens;

import benland100.jcalc.input.Token;

public class Assigner extends Token {
	
	private String type;
	
	public Assigner(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public String toString() {
		return type;
	}
	
}
