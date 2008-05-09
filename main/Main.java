package main;
import java.awt.Robot;

public class Main {
	static Robot r;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			r = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Frame(r);
	}
}
