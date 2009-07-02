package edu.js.appletloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.Project;
import serp.bytecode.ReturnInstruction;

public class LardClassLoader extends URLClassLoader {
	Project p;

	private JarFile overRide;

	URLClassLoader u;

	public LardClassLoader(URL[] urls, JarFile overRide) {
		super(urls);
		this.overRide = overRide;
		try {

			if (overRide != null) {
				this.u = new URLClassLoader(new URL[] { new File(overRide
						.getName()).toURI().toURL() });
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void print() {
		System.out.println("hi");
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class c = null;
		c = super.loadClass(name);
		return c;
	}

	public Class haxxClass(BCClass c) {
		BCClass clazz = c;
		BCMethod loadClass = clazz.getMethods("loadClass", new Class[] {
				String.class, boolean.class })[0];
		Code classLoad = loadClass.getCode(true);
		Instruction temp = null;
		while (temp instanceof ReturnInstruction) {
			temp = classLoad.next();
		}
		classLoad.previousIndex();
		LardClassLoader.print();
		classLoad.astore().setLocal(5);
		classLoad.invokestatic().setMethod(
				p.loadClass(LardClassLoader.class).getMethods("print")[0]);
		classLoad.aload().setLocal(5);
		System.out.println(loadClass.getName());
		clazz.removeDeclaredMethod("loadClass");
		clazz.declareMethod(loadClass);
		return defineClass(clazz.getName(), clazz.toByteArray(), 0, clazz
				.toByteArray().length);
	}
}
