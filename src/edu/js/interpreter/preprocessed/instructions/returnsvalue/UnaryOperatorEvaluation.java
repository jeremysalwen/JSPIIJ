package edu.js.interpreter.preprocessed.instructions.returnsvalue;

import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.FunctionDeclaration;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.tokens.value.OperatorTypes;

public class UnaryOperatorEvaluation implements ReturnsValue {
	public OperatorTypes type;

	public ReturnsValue operon;

	public UnaryOperatorEvaluation(ReturnsValue operon,
			OperatorTypes operator) {
		this.type = operator;
		this.operon = operon;
	}

	public Object get_value(FunctionOnStack f) {
		Object value = operon.get_value(f);
		Class operon_type = value.getClass();
		return type.operate(value);
	}

	@Override
	public String toString() {
		return "operator [" + type + "] on [" + operon + ']';
	}

	public PascalType get_type(FunctionDeclaration f) {
		return operon.get_type( f);
	}

}
