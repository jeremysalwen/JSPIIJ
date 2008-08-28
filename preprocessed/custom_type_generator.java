package preprocessed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		List<variable_declaration> variables = new ArrayList<variable_declaration>();
		variables.add(new variable_declaration("double_field", double.class));
		variables.add(new variable_declaration("integer_field", int.class));
		c.output_class("blah", variables);
	}

	File output;

	public custom_type_generator(File output) {
		this.output = output;
		assert (output.isDirectory());
	}

	public void output_class(String name,
			List<variable_declaration> variables) {
		Project p = new Project();
		BCClass c = p.loadClass("java.lang.Object");
		c.setName(name);
		c.setSuperclass(Object.class);
		c.clearDeclaredMethods();
		c.setDeclaredInterfaces(new Class[] { contains_variables.class });
		c.makePublic();
		c.getSourceFile(true).setFile("dynamically_generated");
		for (variable_declaration v : variables) {
			v.add_declaration(c);
		}
		add_constructor(c);
		add_get_var(c);
		add_set_var(c);
		try {
			c.write(new File(output.getAbsolutePath() + name + ".class"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void add_constructor(BCClass b) {
		BCMethod constructor = b.addDefaultConstructor();
		constructor.removeCode();
		Code constructor_code = constructor.getCode(true);
		constructor_code.aload().setThis();
		try {
			constructor_code.invokespecial().setMethod(
					Object.class.getDeclaredConstructor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (BCField f : b.getFields()) {
			constructor_code.aload().setThis();
			Class<?> field_type = f.getType();
			if (field_type == double.class) {
				constructor_code.constant().setValue(0.0D);
			} else if (field_type == int.class) {
				constructor_code.constant().setValue(0);
			} else if (field_type == float.class) {
				constructor_code.constant().setValue(0.0F);
			} else if (field_type == char.class) {
				constructor_code.constant().setValue('\0');
			} else if (field_type == boolean.class) {
				constructor_code.constant().setValue(false);
			} else {
				try {
					constructor_code.invokespecial().setMethod(
							field_type.getDeclaredConstructor());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			constructor_code.putfield().setField(f);
		}
		constructor_code.vreturn();
		constructor_code.calculateMaxLocals();
		constructor_code.calculateMaxStack();
	}

	void add_get_var(BCClass b) {
		BCMethod get_var = b.declareMethod("get_var", Object.class,
				new Class[] { String.class });
		get_var.makePublic();
		Code get_var_code = get_var.getCode(true);
		get_var_code.aload().setParam(0);
		get_var_code.invokevirtual().setMethod(String.class, "intern",
				String.class, new Class[] {});
		get_var_code.astore().setParam(0);
		JumpInstruction previous_if = null;
		for (BCField f : b.getFields()) {
			Instruction code_block = get_var_code.constant().setValue(
					f.getName());
			if (previous_if != null) {
				previous_if.setTarget(code_block);
			}
			get_var_code.aload().setParam(0);
			previous_if = get_var_code.ifacmpne();
			get_var_code.aload().setThis();
			get_var_code.getfield().setField(f);
			Class return_type = f.getType();
			if (return_type == int.class) {
				get_var_code.invokestatic().setMethod(Integer.class, "valueOf",
						Integer.class, new Class[] { int.class });
			} else if (return_type == double.class) {
				try {
					get_var_code.invokestatic().setMethod(Double.class,
							"valueOf", Double.class,
							new Class[] { double.class });

				} catch (Exception e) {
				}
			} else if (return_type == char.class) {
				get_var_code.invokestatic().setMethod(Character.class,
						"valueOf", Character.class, new Class[] { char.class });
			} else if (return_type == float.class) {
				try {
					get_var_code.invokestatic()
							.setMethod(Float.class, "valueOf", Float.class,
									new Class[] { float.class });
				} catch (Exception e) {
				}
			} else if (return_type == boolean.class) {
				get_var_code.invokestatic().setMethod(Boolean.class, "valueOf",
						Boolean.class, new Class[] { boolean.class });
			}
			get_var_code.areturn();
		}
		Instruction i = get_var_code.constant().setNull();
		if (previous_if != null) {
			previous_if.setTarget(i);
		}
		get_var_code.areturn();
		get_var_code.calculateMaxLocals();
		get_var_code.calculateMaxStack();
	}

	void add_set_var(BCClass b) {
		BCMethod set_var = b.declareMethod("set_var", void.class, new Class[] {
				String.class, Object.class });
		set_var.makePublic();
		Code set_var_code = set_var.getCode(true);
		set_var_code.aload().setParam(0);
		set_var_code.invokevirtual().setMethod(String.class, "intern",
				String.class, new Class[] {});
		set_var_code.astore().setParam(0);
		JumpInstruction previous_if = null;
		for (BCField f : b.getFields()) {
			Instruction jump_to = set_var_code.constant().setValue(f.getName());
			if (previous_if != null) {
				previous_if.setTarget(jump_to);
			}
			set_var_code.aload().setParam(0);
			previous_if = set_var_code.ifacmpne();
			set_var_code.aload().setThis();
			set_var_code.aload().setParam(1);
			Class field_class = f.getType();
			if (field_class == int.class) {
				set_var_code.checkcast().setType(Integer.class);
				set_var_code.invokevirtual().setMethod(Integer.class,
						"intValue", int.class, new Class[] {});
			} else if (field_class == double.class) {
				set_var_code.checkcast().setType(Double.class);
				set_var_code.invokevirtual().setMethod(Double.class,
						"doubleValue", double.class, new Class[] {});
			} else if (field_class == float.class) {
				set_var_code.checkcast().setType(Float.class);
				set_var_code.invokevirtual().setMethod(Float.class,
						"floatValue", float.class, new Class[] {});
			} else if (field_class == boolean.class) {
				set_var_code.checkcast().setType(Boolean.class);
				set_var_code.invokevirtual().setMethod(Boolean.class,
						"booleanValue", boolean.class, new Class[] {});
			} else if (field_class == char.class) {
				set_var_code.checkcast().setType(Character.class);
				set_var_code.invokevirtual().setMethod(Character.class,
						"charValue", char.class, new Class[] {});
			}
			set_var_code.putfield().setField(f);
		}
		Instruction jump_to = set_var_code.vreturn();
		if (previous_if != null) {
			previous_if.setTarget(jump_to);
		}
		set_var_code.calculateMaxLocals();
		set_var_code.calculateMaxStack();
	}
}
