package edu.js.interpreter.preprocessed.interpretingobjects;

import edu.js.interpreter.preprocessed.interpretingobjects.variables.SubvarIdentifier;

public class VariableAcessPointer implements Pointer {
	Object container;
	SubvarIdentifier index;

	public VariableAcessPointer(Object container, SubvarIdentifier index) {
		this.container = container;
		this.index = index;
	}

	@Override
	public Object get() {
		return index.get(container, null);
	}

	@Override
	public void set(Object value) {
		// TODO Auto-generated method stub

	}

}
