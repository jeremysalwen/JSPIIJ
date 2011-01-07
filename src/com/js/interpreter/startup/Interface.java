package com.js.interpreter.startup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.PascalPlugin;
import com.js.interpreter.ast.PluginDeclaration;
import com.js.interpreter.ast.codeunit.ExecutableCodeUnit;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.classgeneration.CustomTypeGenerator;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class Interface {
	public static void main(String[] args) {
		try {
			List<ClassLoader> classloaders = new ArrayList<ClassLoader>();
			classloaders.add(ClassLoader.getSystemClassLoader());
			executeScript("tmp", new FileReader("test.pas"), classloaders,
					new ArrayList<ScriptSource>(0),
					new ArrayList<ScriptSource>(0), new CustomTypeGenerator(
							new File("/tmp/")), new HashMap<String, Object>());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			System.err.println(e.line + ":" + e.getMessage());
		} catch (RuntimePascalException e) {
			System.err.println(e.line + ":" + e.getMessage());
		}
	}

	public static ListMultimap<String, AbstractFunction> loadFunctionTable(
			List<ClassLoader> classloaders, Map<String, Object> context,
			List<ScriptSource> includeSearchPath,
			List<ScriptSource> librarySearchPath,
			CustomTypeGenerator typegenerator) throws ParsingException {
		ListMultimap<String, AbstractFunction> functionTable = ArrayListMultimap
				.create();
		loadPlugins(functionTable, classloaders, context);
		loadLibraries(functionTable, librarySearchPath, includeSearchPath,
				typegenerator);
		return functionTable;
	}

	public static PascalProgram loadPascal(String sourcename, Reader in,
			List<ClassLoader> classloaders,
			List<ScriptSource> includeSearchPath,
			List<ScriptSource> librarySearchPath,
			CustomTypeGenerator typeGenerator, Map<String, Object> context)
			throws ParsingException {
		ListMultimap<String, AbstractFunction> functiontable = loadFunctionTable(
				classloaders, context, includeSearchPath, librarySearchPath,
				typeGenerator);
		return new PascalProgram(in, functiontable, sourcename,
				includeSearchPath, typeGenerator);
	}

	public static void executeScript(String sourcename, Reader in,
			List<ClassLoader> classloaders,
			List<ScriptSource> includeSearchPath,
			List<ScriptSource> librarySearchPath,
			CustomTypeGenerator typeGenerator, Map<String, Object> context)
			throws ParsingException, RuntimePascalException {
		ListMultimap<String, AbstractFunction> functionTable = loadFunctionTable(
				classloaders, context, includeSearchPath, librarySearchPath,
				typeGenerator);
		ExecutableCodeUnit code;
		// long beforetime = System.currentTimeMillis();
		code = new PascalProgram(in, functionTable, sourcename,
				includeSearchPath, typeGenerator);
		// System.out.println("Parse time=" + (System.currentTimeMillis() -
		// beforetime)+" ms");

		RuntimeExecutable<?> runtime = code.run();

		runtime.run();
	}

	public static void loadLibraries(
			ListMultimap<String, AbstractFunction> functionTable,
			List<ScriptSource> librarySearchPath,
			List<ScriptSource> includeSearchPath,
			CustomTypeGenerator type_generator) throws ParsingException {
		for (ScriptSource directory : librarySearchPath) {
			for (String sourcefile : directory.list()) {
				Reader in = directory.read(sourcefile);
				if (in != null) {
					// Automatically adds its definitions to the function table.
					new Library(in, functionTable, sourcefile,
							includeSearchPath, type_generator);
				} else {
					System.err.println("Warning, unable to read library "
							+ sourcefile);
				}

			}
		}
	}

	public static void loadPlugins(
			ListMultimap<String, AbstractFunction> functionTable,
			List<ClassLoader> classloaders, Map<String, Object> pluginContext) {
		for (ClassLoader cl : classloaders) {
			ServiceLoader<PascalPlugin> loader = ServiceLoader.load(
					PascalPlugin.class, cl);
			for (PascalPlugin p : loader) {
				p.instantiate(pluginContext);
				for (Method m : p.getClass().getDeclaredMethods()) {
					if (Modifier.isPublic(m.getModifiers())) {
						PluginDeclaration tmp = new PluginDeclaration(p, m);
						functionTable.put(tmp.name().toLowerCase(), tmp);
					}
				}
			}
		}
		return;
	}

}
