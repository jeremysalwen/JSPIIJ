package tokens;



public class parenthesized_token extends grouper_token {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3945938644412769985L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("(");
		builder.append(next).append(',');
		for (token t : this.queue) {  //TODO check this works
			builder.append(t).append(' ');
		}
		builder.append(')');
		return builder.toString();
	}
}
