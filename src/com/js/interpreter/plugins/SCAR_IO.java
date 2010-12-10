package com.js.interpreter.plugins;

import javax.swing.JOptionPane;

import com.js.interpreter.ast.PascalPlugin;
import com.js.interpreter.gui.IDE;

public class SCAR_IO implements PascalPlugin {
	IDE ide;

	public SCAR_IO(IDE ide) {
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
				"Pascal Interpreter in Java input box", JOptionPane.OK_CANCEL_OPTION);
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
