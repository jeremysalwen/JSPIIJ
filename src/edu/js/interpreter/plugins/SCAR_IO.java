package edu.js.interpreter.plugins;

import javax.swing.JOptionPane;

import edu.js.interpreter.gui.Ide;
import edu.js.interpreter.processing.PascalPlugin;

public class SCAR_IO implements PascalPlugin {
	Ide ide;

	public SCAR_IO(Ide ide) {
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
