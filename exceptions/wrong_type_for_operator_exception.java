package exceptions;

import tokens.operator_types;

public class wrong_type_for_operator_exception extends RuntimeException {
	operator_types operator;

	public wrong_type_for_operator_exception(operator_types t) {
		this.operator = t;
	}

	@Override
	public String toString() {
		return "Incorect operon type for operator " + operator;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5136215612753986685L;

}
