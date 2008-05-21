package preprocessed.interpreting_objects.variables;

import java.util.LinkedList;

public class variable_identifier extends LinkedList<String> {
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String s : this) {
			builder.append(s).append('.');
		}
		return builder.toString();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7674207356042437840L;

}
