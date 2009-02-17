package preprocessed.interpreting_objects.variables;

import java.util.ArrayList;

public class variable_identifier extends ArrayList<subvar_identifier> {
	public variable_identifier() {
		super();
	}

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
		for (Object s : this) {
			builder.append(s).append('.');
		}
		return builder.toString();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7674207356042437840L;

}
