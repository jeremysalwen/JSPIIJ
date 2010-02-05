package edu.js.interpreter.plugins;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_script_control implements pascal_plugin {
	ide ide;

	public scar_script_control(ide i) {
		this.ide = i;
	}

	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.err.println("??? Interrupted.");
			e.printStackTrace();
		}
	}

	public static void wait(int ms) {
		sleep(ms);
	}

	public void terminatescript() {
		ide.stopProgram();
	}

}
