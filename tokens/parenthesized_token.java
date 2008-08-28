package tokens;


public class parenthesized_token extends grouper_token{
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("(");
		for (token t : tokens) {
			builder.append(t).append(' ');
		}
		builder.append(')');
		return builder.toString();
	}
}
