package edu.js.interpreter.plugins.annotations;

public @interface ArrayBoundsInfo{
	public int[] starts() default {};
	public int[] lengths() default {};
}
