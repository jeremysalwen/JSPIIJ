package com.js.interpreter.plugins;

import com.js.interpreter.ast.PascalPlugin;

public class ScriptControl_plugins implements PascalPlugin {

	public ScriptControl_plugins() {
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

	public void performException() {

	}

	public void performException(String message) {
		throw new RuntimeException(message);
	}
}
