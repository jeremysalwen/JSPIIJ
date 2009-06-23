package edu.js.interpreter.plugins;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class standard_io implements pascal_plugin {
	ide ide;

	public standard_io(ide ide) {
		this.ide = ide;
	}

	/**
	 * Redirects to System.out
	 * 
	 * @param s
	 *            The string to output.
	 */
	public void writeln(String s) {
		ide.output_to_debug(s + '\n');
	}

	public void writeln(int s) {
		writeln(String.valueOf(s));
	}

	public void writeln(double s) {
		writeln(String.valueOf(s));
	}
}
