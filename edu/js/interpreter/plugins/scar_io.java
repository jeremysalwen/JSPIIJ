package edu.js.interpreter.plugins;

import javax.swing.JOptionPane;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_io implements pascal_plugin {
	ide ide;

	public scar_io(ide ide) {
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

	public void writeln(char c) {
		writeln(String.valueOf(c));
	}

	public String readln(String message) {
		String result = JOptionPane.showInputDialog(ide, message,
				"pascalinterpreterinjava", JOptionPane.OK_CANCEL_OPTION);
		return result == null ? "" : result;
	}

	public void cleardebug() {
		ide.clearDebug();
	}

	public int getdebuglinecount() {
		return ide.debugBox.getLineCount();
	}

	public void status(String text) {
		ide.status_bar.setText(text);
	}

	public String getstatus() {
		return ide.status_bar.getText();
	}
}
