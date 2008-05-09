package benland100.jcalc.input.tokens;

import benland100.jcalc.input.Token;

public class Constant extends Token {
	
	private double val;
	
	public Constant(String val) {
		try {
			this.val = new Double(val);
		} catch (Exception e) {
			throw new RuntimeException("Bad Constant Value: " + val);
		}
	}
	
	public double getValue() {
		return val;
	}
	
	public String toString() {
		return new Double(val).toString();
	}
	
}
