package com.js.interpreter.plugins.standard;

import java.util.Map;

import com.js.interpreter.plugins.PascalPlugin;

public class ScriptControl_plugins implements PascalPlugin {

	@Override
	public boolean instantiate(Map<String, Object> pluginargs) {
		return true;
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

	public static void performException() {
		throw new RuntimeException();
	}

	public static void performException(String message) {
		throw new RuntimeException(message);
	}
}
