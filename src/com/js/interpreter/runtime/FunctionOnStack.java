package com.js.interpreter.runtime;

import java.util.HashMap;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class FunctionOnStack extends VariableContext {
	public HashMap<String, Object> local_variables = new HashMap<String, Object>();

	public FunctionDeclaration prototype;

	public VariableContext parentContext;

	RuntimeExecutable<?> main;
	@SuppressWarnings("rawtypes")
	HashMap<String, VariableBoxer> reference_variables;

	@SuppressWarnings("rawtypes")
	public FunctionOnStack(VariableContext parentContext,
			RuntimeExecutable<?> main, FunctionDeclaration declaration,
			Object[] arguments) {
		this.prototype = declaration;
		this.parentContext = parentContext;
		this.main=main;
		if (prototype.local_variables != null) {
			for (VariableDeclaration v : prototype.local_variables) {
				v.initialize(local_variables);
			}
		}
		reference_variables = new HashMap<String, VariableBoxer>();
		for (int i = 0; i < arguments.length; i++) {
			if (prototype.isByReference(i)) {
				reference_variables.put(prototype.argument_names[i],
						(VariableBoxer) arguments[i]);
			} else {
				local_variables.put(prototype.argument_names[i], arguments[i]);
			}
		}
		if (declaration.return_type != null) {
			new VariableDeclaration("result", prototype.return_type)
					.initialize(local_variables);
		}
		this.parentContext = parentContext;
		this.prototype = declaration;
	}

	public Object execute() {
		prototype.instructions.execute(this, main);
		return local_variables.get("result");
	}

	public Object getLocalVar(String name) {
		if (local_variables.containsKey(name)) {
			return local_variables.get(name);
		} else if (reference_variables.containsKey(name)) {
			return reference_variables.get(name).get();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean setLocalVar(String name, Object val) {
		if (local_variables.containsKey(name)) {
			local_variables.put(name, val);
		} else if (reference_variables.containsKey(name)) {
			reference_variables.get(name).set(val);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public VariableContext clone() {
		return null;
	}

	@Override
	public VariableContext getParentContext() {
		return parentContext;
	}

}
