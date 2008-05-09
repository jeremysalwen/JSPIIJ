package benland100.jcalc.input;

import java.util.ArrayList;

import benland100.jcalc.input.tokens.Assigner;
import benland100.jcalc.input.tokens.Constant;
import benland100.jcalc.input.tokens.Function;
import benland100.jcalc.input.tokens.FunctionArgs;
import benland100.jcalc.input.tokens.Grouper;
import benland100.jcalc.input.tokens.Operator;
import benland100.jcalc.input.tokens.StringLiteral;
import benland100.jcalc.input.tokens.Variable;

import preProcessing.ReturningProcessedObject;
import processing.CodeProcessor;

public class Parser {

	Token[] tokens;

	CodeProcessor p;

	public Parser(Token[] tokens, CodeProcessor p) {
		this.tokens = tokens;
		this.p = p;
	}

	public double evaluate() {
		for (int i = 0; i < tokens.length; i++) {
			if ((tokens[i] instanceof Assigner)
					&& (((Assigner) tokens[i]).getType().equals(">>"))) {
				Token[] newTokens = new Token[i + 1];
				System.arraycopy(tokens, 0, newTokens, 0, i + 1);
				return ((pascalTypes.VarDouble) (p
						.getVar(((Variable) tokens[i + 1]).getName()))).get();
			}
		}
		return evaluate(simplify(tokens));
	}

	private double evaluate(Object[] MathmaticalData) {
		ArrayList<Object> Data = new ArrayList<Object>();
		Object[] data = MathmaticalData;
		Double last = null;
		for (int i = 0; i < data.length;) {
			double a = 0.0;
			String op = "";
			try {
				if ((data[i] instanceof String)
						&& (data[i + 1] instanceof Double)
						&& ((i == 0) || (data[i - 1] instanceof String))) {
					op = (String) data[i];
					a = (Double) data[i + 1];
					i += 2;
				}
			} catch (Exception e) {
			}
			if (op.equals("-")) {
				last = a * -1;
			} else if (op.equals("+")) {
				last = Math.abs(a);
			}
			if (last != null) {
				Data.add(last);
				last = null;
			} else {
				Data.add(data[i++]);
			}
		}
		data = Data.toArray(new Object[0]);
		Data.clear();
		last = null;
		for (int i = 0; i < data.length;) {
			double a = 0.0;
			String op = "";
			double b = 0.0;
			try {
				Double aD = (Double) data[i];
				a = last == null ? aD : last;
				op = (String) data[++i];
				Double bD = (Double) data[++i];
				b = bD;
			} catch (Exception e) {
			}
			if (op.equals("^")) {
				last = Math.pow(a, b);
				if (last.isNaN())
					last = 0.0;
			} else {
				Data.add(a);
				Data.add(op);
				last = null;
			}
			if (i + 1 >= data.length) {
				Data.add(last == null ? b : last);
				break;
			}
		}
		data = Data.toArray(new Object[0]);
		Data.clear();
		last = null;
		// mod, truths
		for (int i = 0; i < data.length;) {
			double a = 0.0;
			String op = "";
			double b = 0.0;
			try {
				Double aD = (Double) data[i];
				a = last == null ? aD : last;
				op = (String) data[++i];
				Double bD = (Double) data[++i];
				b = bD;
			} catch (Exception e) {
			}
			if (op.equals("*")) {
				last = a * b;
			} else if (op.equals("/")) {
				last = a / b;
			} else if (op.equals("%")) {
				last = a % b;
			} else if (op.equals(">")) {
				last = a > b ? 1.0 : 0.0;
			} else if (op.equals("<")) {
				last = a < b ? 1.0 : 0.0;
			} else if (op.equals("=")) {
				last = a == b ? 1.0 : 0.0;
			} else if (op.equals("#")) {
				last = a != b ? 1.0 : 0.0;
			} else {
				Data.add(a);
				Data.add(op);
				last = null;
			}
			if (i + 1 >= data.length) {
				Data.add(last == null ? b : last);
				break;
			}
		}
		data = Data.toArray(new Object[0]);
		last = null;
		for (int i = 0; i < data.length;) {
			double a = 0.0;
			String op = "";
			double b = 0.0;
			try {
				Double aD = (Double) data[i];
				a = last == null ? aD : last;
				op = (String) data[++i];
				Double bD = (Double) data[++i];
				b = bD;
			} catch (Exception e) {
			}
			if (op.equals("+")) {
				last = a + b;
			} else if (op.equals("-")) {
				last = a - b;
			}
			if (i + 1 >= data.length) {
				last = new Double(last == null ? a : last);
				break;
			}
		}
		return last == null ? 0.0 : last;
	}

	private Object[] simplify(Token[] tokens) {
		ArrayList<Object> Data = new ArrayList<Object>();
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] instanceof Constant) {
				Data.add(((Constant) tokens[i]).getValue());
			} else if (tokens[i] instanceof Operator) {
				Data.add(((Operator) tokens[i]).getType());
			} else if (tokens[i] instanceof Variable) {
				Data.add(((Variable) tokens[i]).getValue(p));
			} else if (tokens[i] instanceof Grouper) {
				int p = 0;
				Token[] newTokens = null;
				for (int n = i + 1; n < tokens.length; n++) {
					if (tokens[n] instanceof Grouper) {
						if (((Grouper) tokens[n]).getType().equals("(")) {
							p++;
							continue;
						} else if ((((Grouper) tokens[n]).getType().equals(")"))
								&& (p-- == 0)) {
							int start = i + 1;
							int end = n;
							newTokens = new Token[end - start];
							System.arraycopy(tokens, start, newTokens, 0, end
									- start);
							i = n;
							break;
						}
					}
				}
				if (newTokens == null)
					throw new RuntimeException("Mathmatical Format Error");
				Data.add(new Parser(newTokens, this.p).evaluate());
			} else if (tokens[i] instanceof Function) {
				ReturningProcessedObject[] s = (ReturningProcessedObject[]) ((FunctionArgs) tokens[++i])
						.getArgs().toArray();
				Data.add(((Function) tokens[i - 1]).evaluate(s, p));
			} else if (tokens[i] instanceof StringLiteral) {
				Data.add(((StringLiteral) tokens[i]).toString());
			}
		}
		return Data.toArray(new Object[0]);
	}
}
