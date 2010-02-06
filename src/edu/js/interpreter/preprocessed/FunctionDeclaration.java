package edu.js.interpreter.preprocessed;

import java.util.ArrayList;
import java.util.List;

import edu.js.interpreter.pascaltypes.PascalType;
import edu.js.interpreter.preprocessed.instructions.DowntoForStatement;
import edu.js.interpreter.preprocessed.instructions.Executable;
import edu.js.interpreter.preprocessed.instructions.ForStatement;
import edu.js.interpreter.preprocessed.instructions.IfStatement;
import edu.js.interpreter.preprocessed.instructions.InstructionGrouper;
import edu.js.interpreter.preprocessed.instructions.RepeatInstruction;
import edu.js.interpreter.preprocessed.instructions.VariableSet;
import edu.js.interpreter.preprocessed.instructions.WhileStatement;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.BinaryOperatorEvaluation;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.BuiltinTypeConversion;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ConstantAccess;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.ReturnsValue;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.UnaryOperatorEvaluation;
import edu.js.interpreter.preprocessed.instructions.returnsvalue.VariableAccess;
import edu.js.interpreter.preprocessed.interpretingobjects.FunctionOnStack;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.ReturnsValue_SubvarIdentifier;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.String_SubvarIdentifier;
import edu.js.interpreter.preprocessed.interpretingobjects.variables.VariableIdentifier;
import edu.js.interpreter.processing.StaticMethods;
import edu.js.interpreter.processing.PascalProgram;
import edu.js.interpreter.tokens.EOF_Token;
import edu.js.interpreter.tokens.Token;
import edu.js.interpreter.tokens.basic.AssignmentToken;
import edu.js.interpreter.tokens.basic.ColonToken;
import edu.js.interpreter.tokens.basic.CommaToken;
import edu.js.interpreter.tokens.basic.DoToken;
import edu.js.interpreter.tokens.basic.DowntoToken;
import edu.js.interpreter.tokens.basic.ElseToken;
import edu.js.interpreter.tokens.basic.ForToken;
import edu.js.interpreter.tokens.basic.IfToken;
import edu.js.interpreter.tokens.basic.PeriodToken;
import edu.js.interpreter.tokens.basic.RepeatToken;
import edu.js.interpreter.tokens.basic.SemicolonToken;
import edu.js.interpreter.tokens.basic.ThenToken;
import edu.js.interpreter.tokens.basic.ToToken;
import edu.js.interpreter.tokens.basic.UntilToken;
import edu.js.interpreter.tokens.basic.VarToken;
import edu.js.interpreter.tokens.basic.WhileToken;
import edu.js.interpreter.tokens.grouping.BeginEndToken;
import edu.js.interpreter.tokens.grouping.BracketedToken;
import edu.js.interpreter.tokens.grouping.GrouperToken;
import edu.js.interpreter.tokens.grouping.ParenthesizedToken;
import edu.js.interpreter.tokens.value.DoubleToken;
import edu.js.interpreter.tokens.value.IntegerToken;
import edu.js.interpreter.tokens.value.OperatorToken;
import edu.js.interpreter.tokens.value.StringToken;
import edu.js.interpreter.tokens.value.WordToken;

public class FunctionDeclaration extends AbstractFunction {
	public PascalProgram program;

	public String name;

	public List<VariableDeclaration> local_variables;

	public Executable instructions;

	public PascalType return_type;

	/* These go together ----> */
	public String[] argument_names;

	public PascalType[] argument_types;

	public boolean[] are_varargs;

	/* <----- */

	public FunctionDeclaration(PascalProgram p, GrouperToken i,
			boolean is_procedure) {
		this.program = p;
		instructions = new InstructionGrouper();
		name = program.get_word_value(i);
		get_arguments_for_declaration(i, is_procedure);
		Token next = i.peek();
		assert (is_procedure ^ (next instanceof ColonToken));
		if (!is_procedure && next instanceof ColonToken) {
			try {
				i.take();
				return_type = program.get_next_pascal_type(i);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		program.assert_next_semicolon(i);
		next = i.peek();
		if (next instanceof VarToken) {
			i.take();
			local_variables = program.get_variable_declarations(i);
		} else {
			local_variables = new ArrayList<VariableDeclaration>();
		}
		instructions = get_next_command(i);
		program.assert_next_semicolon(i);
	}

	public void add_local_variable(VariableDeclaration v) {
		local_variables.add(v);
	}

	public FunctionDeclaration(PascalProgram p) {
		this.program = p;
		this.local_variables = new ArrayList<VariableDeclaration>();
	}

	public FunctionDeclaration(List<VariableDeclaration> local_variables,
			InstructionGrouper instructions) {
		this.local_variables = local_variables;
		this.instructions = instructions;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Object call(PascalProgram program, Object[] arguments) {
		return new FunctionOnStack(program, this, arguments).execute();
	}

	public PascalType get_variable_type(String name) {
		if(name.equalsIgnoreCase("result")) {
			return return_type;
		}
		int index = StaticMethods.indexOf(argument_names, name);
		if (index != -1) {
			return argument_types[index];
		} else {
			for (VariableDeclaration v : local_variables) {
				if (v.name.equals(name)) {
					return v.type;
				}
			}
		}
		return null;
	}

	@Override
	public boolean is_varargs(int i) {
		return are_varargs[i];
	}

	@Override
	public String toString() {
		return super.toString() + ",function";
	}

	private void get_arguments_for_declaration(GrouperToken i,
			boolean is_procedure) { // need
		List<Boolean> are_varargs_list = new ArrayList<Boolean>();
		List<String> names_list = new ArrayList<String>();
		List<PascalType> types_list = new ArrayList<PascalType>();
		Token next = i.take();
		if (next instanceof ParenthesizedToken) {
			ParenthesizedToken arguments_token = (ParenthesizedToken) next;
			while (arguments_token.hasNext()) {
				int j = 0; // counts number added of this type
				next = arguments_token.take();
				boolean is_varargs = false;
				if (next instanceof VarToken) {
					is_varargs = true;
					next = arguments_token.take();
				}
				do {
					are_varargs_list.add(is_varargs);
					names_list.add(((WordToken) next).name);
					j++;
				} while ((next = arguments_token.take()) instanceof CommaToken);

				assert (next instanceof ColonToken);
				PascalType type;
				try {
					type = program.get_next_pascal_type(arguments_token);
					while (j > 0) {
						types_list.add(type);
						j--;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		argument_types = types_list.toArray(new PascalType[types_list.size()]);
		argument_names = names_list.toArray(new String[names_list.size()]);
		are_varargs = new boolean[are_varargs_list.size()];
		for (int k = 0; k < are_varargs.length; k++) {
			are_varargs[k] = are_varargs_list.get(k);
		}
	}

	ReturnsValue get_next_returns_value(GrouperToken iterator) {
		Token next = iterator.take();
		if (next instanceof OperatorToken) {
			assert ((OperatorToken) next).can_be_unary();
			return new UnaryOperatorEvaluation(
					get_next_returns_value(iterator),
					((OperatorToken) next).type);
		}
		ReturnsValue result = null;
		if (next instanceof ParenthesizedToken) {
			result = get_single_value((ParenthesizedToken) next);
		} else if (next instanceof IntegerToken) {
			result = new ConstantAccess(((IntegerToken) next).value);
		} else if (next instanceof DoubleToken) {
			result = new ConstantAccess(((DoubleToken) next).value);
		} else if (next instanceof StringToken) {
			result = new ConstantAccess(new StringBuilder(
					((StringToken) next).value));
		} else if (next instanceof WordToken) {
			String name = ((WordToken) next).name;
			if (!((next = iterator.peek()) instanceof EOF_Token)) {
				if (next instanceof ParenthesizedToken) {
					ReturnsValue[] arguments = get_arguments_for_call((ParenthesizedToken) iterator
							.take());
					result = program.generate_function_call(name, arguments,
							this);
				} else {
					result = new VariableAccess(get_next_var_identifier(name,
							iterator));
				}
			} else {
				result = new VariableAccess(get_next_var_identifier(name,
						iterator));
			}
		}
		assert (result != null);
		if ((next = iterator.peek()) instanceof OperatorToken) {
			iterator.take();
			result = new BinaryOperatorEvaluation(result,
					get_next_returns_value(iterator),
					((OperatorToken) next).type);
		}
		return result;
	}

	VariableIdentifier get_next_var_identifier(GrouperToken i) {
		Token next;
		VariableIdentifier identifier = new VariableIdentifier();
		identifier.add(new String_SubvarIdentifier(program.get_word_value(i)));
		while (true) {
			if (i.peek() instanceof PeriodToken) {
				i.take();
				next = i.take();
				if (next instanceof WordToken) {
					identifier.add(new String_SubvarIdentifier(
							((WordToken) next).name));
				} else {
					program.error();
				}
			} else if (i.peek() instanceof BracketedToken) {
				identifier.add(new ReturnsValue_SubvarIdentifier(
						get_next_returns_value((BracketedToken) i.take())));
			} else {
				break;
			}
		}
		return identifier;
	}

	VariableIdentifier get_next_var_identifier(String initial, GrouperToken i) {
		Token next;
		VariableIdentifier identifier = new VariableIdentifier();
		identifier.add(new String_SubvarIdentifier(initial));
		while (true) {
			if (i.peek() instanceof PeriodToken) {
				i.take();
				next = i.take();
				if (next instanceof WordToken) {
					identifier.add(new String_SubvarIdentifier(
							((WordToken) next).name));
				} else {
					program.error();
				}
			} else if (i.peek() instanceof BracketedToken) {
				identifier.add(new ReturnsValue_SubvarIdentifier(
						get_next_returns_value((BracketedToken) i.take())));
			} else {
				break;
			}
		}
		return identifier;
	}

	PascalType[] returns_values_to_pascal_types(ReturnsValue[] values) {
		PascalType[] result = new PascalType[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = values[i].get_type(this);
		}
		return result;
	}

	public Executable get_next_command(GrouperToken token_iterator) {
		Token next = token_iterator.take();
		if (next instanceof IfToken) {
			ReturnsValue condition = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			assert (next instanceof ThenToken);
			Executable command = get_next_command(token_iterator);
			Executable else_command = null;
			next = token_iterator.peek();
			if (next instanceof ElseToken) {
				token_iterator.take();
				else_command = get_next_command(token_iterator);
			}
			assert (next instanceof SemicolonToken);
			return new IfStatement(condition, command, else_command);
		} else if (next instanceof WhileToken) {
			ReturnsValue condition = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			assert (next instanceof DoToken);
			Executable command = get_next_command(token_iterator);
			program.assert_next_semicolon(token_iterator);
			return new WhileStatement(condition, command);
		} else if (next instanceof BeginEndToken) {
			InstructionGrouper begin_end_preprocessed = new InstructionGrouper();
			BeginEndToken cast_token = (BeginEndToken) next;
			while (cast_token.hasNext()) {
				begin_end_preprocessed
						.add_command(get_next_command(cast_token));
				program.assert_next_semicolon(cast_token);
			}
			return begin_end_preprocessed;
		} else if (next instanceof ForToken) {
			VariableIdentifier tmp_var = get_next_var_identifier(token_iterator);
			next = token_iterator.take();
			assert (next instanceof AssignmentToken);
			ReturnsValue first_value = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			boolean downto = false;
			if (next instanceof DowntoToken) {
				downto = true;
			} else if (!(next instanceof ToToken)) {
				System.err.println("Expected to or downto");
				System.exit(1);
			}
			ReturnsValue last_value = get_next_returns_value(token_iterator);
			next = token_iterator.take();
			assert (next instanceof DoToken);
			Executable result;
			if (downto) { // TODO probably should merge these two types
				result = new DowntoForStatement(tmp_var, first_value,
						last_value, get_next_command(token_iterator));
			} else {
				result = new ForStatement(tmp_var, first_value, last_value,
						get_next_command(token_iterator));
			}
			return result;
		} else if (next instanceof RepeatToken) {
			InstructionGrouper command = new InstructionGrouper();
			while (!(token_iterator.peek_no_EOF() instanceof UntilToken)) {
				get_next_command(token_iterator);
			}
			next = token_iterator.take();
			assert (next instanceof UntilToken);
			ReturnsValue condition = get_next_returns_value(token_iterator);
			return new RepeatInstruction(command, condition);
		} else if (next instanceof WordToken) {
			String name = program.get_word_value(next);
			next = token_iterator.peek_no_EOF();
			if (next instanceof ParenthesizedToken) {
				token_iterator.take();
				ReturnsValue[] arguments = get_arguments_for_call((ParenthesizedToken) next);
				return program.generate_function_call(
						name, arguments, this);
			} else {
				// at this point assuming it is a variable identifier.
				VariableIdentifier identifier = get_next_var_identifier(name,
						token_iterator);
				next = token_iterator.take();
				assert (next instanceof AssignmentToken);
				ReturnsValue value_to_assign = get_next_returns_value(token_iterator);
				PascalType output_type=new VariableAccess(identifier).get_type(this);
				if(!output_type.equals(value_to_assign.get_type(this))) {
					value_to_assign=new BuiltinTypeConversion(output_type, value_to_assign);
				}
				return new VariableSet(identifier, value_to_assign);
			}
		}
		System.err
				.println("Could not identify token "
						+ next
						+ ", because it did not match any of the criteria for an instruction");
		return null;
	}

	ReturnsValue[] get_arguments_for_call(ParenthesizedToken t) {
		List<ReturnsValue> result = new ArrayList<ReturnsValue>();
		while (t.hasNext()) {
			result.add(get_next_returns_value(t));
			if (t.hasNext()) {
				Token next = t.take();
				assert (next instanceof CommaToken);
			}
		}
		return result.toArray(new ReturnsValue[result.size()]);
	}

	ReturnsValue get_single_value(ParenthesizedToken t) {
		ReturnsValue result = get_next_returns_value(t);
		assert (!t.hasNext());
		return result;
	}

	private List<Executable> get_function_body(BeginEndToken t) {
		List<Executable> commands = new ArrayList<Executable>();
		while (t.hasNext()) {
			commands.add(get_next_command(t));
			program.assert_next_semicolon(t);
		}
		return commands;
	}

	@Override
	public PascalType[] argument_types() {
		return argument_types;
	}

	@Override
	public PascalType return_type() {
		return return_type;
	}
}
