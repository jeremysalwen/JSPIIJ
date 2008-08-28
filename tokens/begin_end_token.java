package tokens;


public class begin_end_token extends grouper_token {

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("begin ");
		for (token t : tokens) {
			builder.append(t).append(' ');
		}
		builder.append("end ");
		return builder.toString();
	}
}
