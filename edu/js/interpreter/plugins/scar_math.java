package edu.js.interpreter.plugins;

import java.util.Random;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class scar_math implements pascal_plugin {
	ide ide;

	Random r;

	public scar_math(ide i) {
		this.ide = i;
		r = new Random();
	}

	public static int ceil(double d) {
		return (int) Math.ceil(d);
	}

	public static int floor(double d) {
		return (int) Math.floor(d);
	}

	public static int round(double d) {
		return (int) Math.round(d);
	}

	public int random(int range) {
		return r.nextInt(range);
	}

	public static int getsystemtime() {
		return (int) System.currentTimeMillis();
	}

	public static double pow(double base, double exponent) {
		return Math.pow(base, exponent);
	}

	public static double sin(double d) {
		return Math.sin(d);
	}

	public static double cos(double d) {
		return Math.cos(d);
	}

	public static double sqr(double d) {
		return d * d;
	}

	public static double sqrt(double d) {
		return Math.sqrt(d);
	}

	public static boolean samevalue(double a, double b) {
		return a == b;
	}
}
