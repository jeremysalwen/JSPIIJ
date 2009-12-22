package edu.js.interpreter.plugins.annotations;

public @interface array_bounds_info{
	public int[] starts() default {};
	public int[] lengths() default {};
}
