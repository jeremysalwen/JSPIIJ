package benland100.jcalc.input.tokens;

import benland100.jcalc.input.Token;

public class StringLiteral extends Token {
	
	private String dat;
	
	public StringLiteral(String dat) {
		this.dat = dat.substring(1, dat.length() - 1);
	}
	
	public String toString () {
		return new String(dat);
	}	
}
