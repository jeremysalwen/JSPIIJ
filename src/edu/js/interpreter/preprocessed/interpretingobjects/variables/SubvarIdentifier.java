package edu.js.interpreter.preprocessed.interpretingobjects.variables;

import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;

public interface SubvarIdentifier {
	public Object get(Object container, FunctionOnStack context);

	public void set(Object container, FunctionOnStack context, Object value);

	public Pointer create_pointer(Object container, FunctionOnStack context);

	public PascalType getType(PascalType containerType);

}
