package tokens.grouping;

import tokens.token;



public class begin_end_token extends grouper_token {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783725988847512384L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("begin ");
		builder.append(next);
		for (token t : this.queue) {//TODO check this doesn't modify
			builder.append(t).append(' ');
		}
		builder.append("end ");
		return builder.toString();
	}
}
