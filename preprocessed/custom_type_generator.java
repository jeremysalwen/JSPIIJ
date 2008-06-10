package preprocessed;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import preprocessed.interpreting_objects.variables.contains_variables;
import serp.bytecode.BCClass;
import serp.bytecode.BCField;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.JumpInstruction;
import serp.bytecode.Project;

public class custom_type_generator {
	public static void main(String[] args) {
		custom_type_generator c = new custom_type_generator(new File("C:\\"));
		LinkedList<variable_declaration> variables = new LinkedList<variable_declaration>();
		variables.add(new variable_declaration("x", Integer.class));
		variables.add(new variable_declaration("y", Integer.class));
		c.output_class("blah", variables);
	}

	File output;

	public custom_type_generator(File output) {
		this.output = output;
		assert (output.isDirectory());
	}

	public void output_class(String name,
			LinkedList<variable_declaration> variables) {
		Project p = new Project();
		BCClass c = p.loadClass("java.lang.Object");
		c.setName(name);
		c.setSuperclass(Object.class);
		c.clearDeclaredMethods();
		c.setDeclaredInterfaces(new Class[] { contains_variables.class });
		c.makePublic();
		for (variable_declaration v : variables) {
			v.add_declaration(c);
		}
		BCMethod get_var = c.declareMethod("get_var", Object.class,
				new Class[] { String.class });
		get_var.makePublic();
		Code method_code = get_var.getCode(true);
		method_code.aload().setLocal(1);
		method_code.invokevirtual().setMethod("intern", String.class,
				new Class[] {});
		method_code.astore().setLocal(1);
		JumpInstruction previous_if = null;
		for (BCField f : c.getFields()) {
			Instruction code_block = method_code.aload().setLocal(1);
			if (previous_if != null) {
				previous_if.setTarget(code_block);
			}
			method_code.constant().setValue(f.getName());
			previous_if = method_code.ifacmpne();
			method_code.getfield().setField(f);
			method_code.areturn();
		}
		Instruction i = method_code.constant().setNull();
		if (previous_if != null) {
			previous_if.setTarget(i);
		}
		method_code.areturn();
		method_code.calculateMaxLocals();
		method_code.calculateMaxStack();
		try {
			c.write(new File(output.getAbsolutePath() + name + ".class"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
