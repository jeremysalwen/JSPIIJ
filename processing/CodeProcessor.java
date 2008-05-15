package processing;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JTextArea;

import pascal_types.standard_type;
import pascal_types.standard_var;
import pascal_types.types;

public class CodeProcessor implements Runnable {
	public Stack<variableStorage> FunctionVariables = new Stack<variableStorage>();

	public PreprocessedFunction[] functions;

	public static void main(String[] args) {
		try {
			CodeProcessor c = new CodeProcessor(null);
			c.loadPlugins();
			long l = System.currentTimeMillis();
			String s = "var:  \n" + "  s, p:string; \n x:double";
			c.parseVariables(s);
			System.out.println(c.getValue("x"));
			long k = System.currentTimeMillis() - l;
			System.out.println(k);

			VarString var = (VarString) c.getVar("p");
			var.set("omg");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean isAssignment(String line) {
		if (!line.contains(":="))
			return false;
		for (int i = 0; i < line.length(); i++) {
			boolean quotes = false;
			if (line.charAt(i) == '\'')
				quotes = !quotes;
			if (line.substring(i, i + 2).equals(":="))
				if (!quotes) {
					String variableName = line.substring(0, i);
					if (getVar(variableName) != null)
						if (line.endsWith(";")) {
							types valueType = getExpressionType(line.substring(
									i + 2, line.length() - 1));
							switch (getVar(variableName).getType()) {
							case VarChar:
								if (valueType.equals(types.Char)
										|| valueType.equals(types.VarChar))
									return true;
							case VarInt:
								if (valueType.equals(types.Int)
										|| valueType.equals(types.VarInt))
									return true;
							case VarString:
								if (valueType.equals(types.string)
										|| valueType.equals(types.VarString))
									return true;
							case VarDouble:
								if (valueType.equals(types.Double)
										|| valueType.equals(types.VarDouble))
									return true;
							}
						}
				}
		}
		return false;
	}

	private void parseVariables(String s) {
		if (!s.trim().startsWith("var:"))
			throw new IllegalArgumentException(
					"You tried to parse variables from a place there were none!");
		String whatsLeft = s.substring(s.toLowerCase().indexOf("var:") + 5,
				s.length()).trim();
		ArrayList<String> varNames = new ArrayList<String>();
		while (whatsLeft.length() > 0)
			for (int i = 0; i < whatsLeft.length(); i++) {
				char c = whatsLeft.charAt(i);
				if (c == ':' || c == ',' || c == ';') {
					String leftSide = whatsLeft.substring(0, i);
					switch (c) {
					case ':':
						varNames.add(leftSide);
						break;
					case ',':
						varNames.add(leftSide);
						break;
					case ';':
						if (leftSide.equalsIgnoreCase("integer"))
							for (String varName : varNames)
								variables.add(new VarInt(0, varName));
						else if (leftSide.equalsIgnoreCase("string"))
							for (String varName : varNames)
								variables.add(new VarString("", varName));
						else if (leftSide.equalsIgnoreCase("character"))
							for (String varName : varNames)
								variables.add(new VarChar(' ', varName));
						varNames.clear();
						break;
					}
					whatsLeft = whatsLeft.substring(i + 1, whatsLeft.length())
							.trim();
					break;
				}
			}
	}

	JTextArea text;

	public Bool keepRunning;

	public Bool pause;

	String currentLine;

	public ArrayList<Class<pascalPlugin>> plugins = new ArrayList<Class<pascalPlugin>>();

	public variableStorage variables = new variableStorage();

	public CodeProcessor(JTextArea text) {
		this.text = text;
		keepRunning = new Bool(true);
	}

	public void run() {
		processCode();
	}

	private void processCode() {
		Scanner s = new Scanner(text.getText());
		s.useDelimiter("\n");
		while (s.hasNext()) {
			currentLine = s.next();
			if (currentLine.trim().startsWith("//"))
				continue;
		}
	}

	public ArrayList<standard_type> parseArgs(String s) {
		ArrayList<standard_type> result = new ArrayList<standard_type>();
		if (!(s.contains("(") && s.contains(")")))
			return result;
		String theString = s.substring(1, s.length());
		String[] split = { "," };
		String[] argSplit = StaticMethods.splitNoQuotes(theString, split);
		for (String s2 : argSplit) {
			s2 = s2.trim().toLowerCase();
			standard_var v = getVar(s2);
			if (v != null)
				result.add(v);
			else {
				standard_type p = parseConst(s2);
				if (p != null)
					result.add(p);
				else if (isPlugin(s2))
					try {
						result.add(getPluginVal(s2));
					} catch (Exception ex) {

						System.out
								.println("Bad plugin found... from the line: "
										+ s2);
					}
				else if (isFunction(s2))
					result.add(getFunctionVal(s2));
				else if (getExpressionType(s2).equals(types.Double))
					result.add(getValue(s2));
			}
		}
		return result;
	}

	private boolean isValidLine(String s) {
		if (isPlugin(s.substring(0, s.length() - 1)))
			return true;
		return false;
	}

	public standard_var getVar(String s) {
		return variables.get(s);
	}

	private standard_type parseConst(String s) {
		if (s.startsWith("'") && s.endsWith("'"))
			return new string_token(s.substring(1, s.length() - 1));
		else
			try {
				return new Int(Integer.parseInt(s));
			} catch (NumberFormatException e) {
				try {
					return new Double(java.lang.Double.parseDouble(s));
				} catch (NumberFormatException ex) {
					return null;
				}
			}
	}

	private boolean isPlugin(String s) {
		ArrayList<standard_type> args;
		if (s.contains("(") && s.contains(")"))
			args = parseArgs(s.substring(s.indexOf('('), StaticMethods
					.lastIndex(s, ')') + 1));
		else
			args = new ArrayList<standard_type>();
		Class<pascalPlugin> foundPlugin = getPlugin(s);
		if (foundPlugin == null)
			return false;
		return StaticMethods.argsMatch(args, foundPlugin);
	}

	public standard_type getPluginVal(String name, ArrayList<standard_type> args) {
		Class<pascalPlugin> foundPlugin = getPlugin(name);
		return getPluginVal(foundPlugin, args);
	}

	public standard_type getPluginVal(Class<pascalPlugin> foundPlugin,
			ArrayList args) {
		Object o = null;
		try {
			Class c = new ArrayList<standard_type>().getClass();
			Constructor con = foundPlugin.getConstructor(c);
			try {
				o = con.newInstance(new Object[] { args });
			} catch (Exception e) {
				throw new RuntimeException("Cannot Instantaite", e);
			}
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException("Constructor not found", ex);
		}
		Method m = null;
		try {
			m = foundPlugin.getMethod("process");
		} catch (Exception e) {
			throw new RuntimeException("Required method not found");
		}
		try {
			return (standard_type) m.invoke(o);
		} catch (Exception e) {
			throw new RuntimeException("Unable to invoke method");
		}

	}

	public standard_type getPluginVal(String s) throws Exception {
		if (!isPlugin(s))
			throw new IllegalArgumentException(
					"check before accessing a plugin");
		ArrayList<standard_type> args = parseArgs(s.substring(s.indexOf('('),
				StaticMethods.lastIndex(s, ')') + 1));
		Class<pascalPlugin> foundPlugin = getPlugin(s);
		return getPluginVal(s, args);
	}

	private standard_type getFunctionVal(String s2) {
		// TODO dat
		return null;
	}

	private boolean isFunction(String s2) {
		// TODO THISSS!!!
		return false;
	}

	private void loadPlugins() {
		File pluginFolder = new File("plugins/");
		File[] pluginarray = pluginFolder.listFiles(new java.io.FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".class");
			}
		});
		ArrayList<Class> classes = new ArrayList<Class>();
		ClassLoader c = ClassLoader.getSystemClassLoader();
		for (File f : pluginarray)
			try {
				String name = f.getName().substring(0,
						f.getName().indexOf(".class"));
				classes.add(c.loadClass("plugins." + name));
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		for (Class c2 : classes)
			if (!StaticMethods.isSubClass(c2, pascalPlugin.class))
				classes.remove(c2);
		for (Class c3 : classes) {
			plugins.add(c3);
			System.out.println(c3.getName());
		}
	}

	public standard_type getValue(String text) {
		types x = getExpressionType(text);
		if (x == null)
			return null;
		switch (x) {
		case VarChar:
		case VarString:
			standard_type p = getVar(text);
			if (p != null)
				return p;
		case Char:
		case string:
			p = parseConst(text);
			if (p != null)
				return p;
			if (isPlugin(text))
				try {
					return getPluginVal(text);
				} catch (Exception e) {
					System.out.println("bad plugin found in text: " + text);
				}
			if (isFunction(text))
				return getFunctionVal(text);
		case VarInt:
			p = getVar(text);
			if (p != null)
				return p;
		case Int:
			p = parseConst(text);
			if (p != null)
				return p;
			if (isPlugin(text))
				try {
					return getPluginVal(text);
				} catch (Exception e) {
					System.out.println("bad plugin found in text: " + text);
				}
			if (isFunction(text))
				return getFunctionVal(text);
			return new Int((int) new Parser(new Lexer(text, this).tokens(),
					this).evaluate());
		case VarDouble:
			p = getVar(text);
			if (p != null)
				return p;
		case Double:
			p = parseConst(text);
			if (p != null)
				return p;
			if (isPlugin(text))
				try {
					return getPluginVal(text);
				} catch (Exception e) {
					System.out.println("bad plugin found in text: " + text);
				}
			if (isFunction(text))
				return getFunctionVal(text);
			return new Double(new Parser(new Lexer(text, this).tokens(), this)
					.evaluate());
		}
		return null;
	}

	public types getPluginReturnType(Class<pascalPlugin> type) {
		try {

			return types.valueOf(type.getMethod("process", new Class[0])
					.getReturnType().getSimpleName());
		} catch (Exception e) {
			System.out.println("error!");
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	public types getExpressionType(String expression) {
		if (isPlugin(expression))
			return getPluginReturnType(getPlugin(expression));
		standard_type p = parseConst(expression);
		if (p != null)
			return p.getType();
		p = getVar(expression);
		if (p != null)
			return p.getType();
		try {
			new Lexer(expression, this);
			return types.Double;
		} catch (Exception e) {
		}
		return null;
	}

	public Class<pascalPlugin> getPlugin(String expression) {
		String supposedName;
		if (expression.contains("("))
			supposedName = expression.substring(0, expression.indexOf('('));
		else
			supposedName = expression;
		Class<pascalPlugin> foundPlugin = null;
		for (Class<pascalPlugin> p : plugins)
			if (supposedName.equalsIgnoreCase(p.getSimpleName()))
				foundPlugin = p;
		return foundPlugin;
	}

	public boolean isValidExpression(String expression) {
		if (isPlugin(expression))
			return true;
		if (parseConst(expression) != null)
			return true;
		if (getVar(expression) != null)
			return true;
		try {
			new Parser(new Lexer(expression, this).tokens(), this).evaluate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
