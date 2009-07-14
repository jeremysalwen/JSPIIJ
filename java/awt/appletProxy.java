package java.awt;

import java.applet.Applet;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.net.URL;


public class appletProxy extends Applet implements FocusListener,
		MouseListener, MouseMotionListener, KeyListener, InputMethodListener{
	public Applet realApplet;

	public Component canvas;

	BufferedImage display;

	public appletProxy(Applet a) {
		this.realApplet = a;
		if (a.getHeight() != 0 && a.getWidth() != 0) {
			display = new BufferedImage(a.getWidth(), a.getHeight(),
					BufferedImage.TYPE_INT_RGB);
		}
		this.addFocusListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.addInputMethodListener(this);
	}

	public void updateCanvas() {
		canvas = realApplet.getComponents()[0];
	}

	public void addEventFilter() {
		EventQueue queue = realApplet.getToolkit().getSystemEventQueue();
		Field eventThread = null;
		try {
			eventThread = EventQueue.class.getDeclaredField("dispatchThread");
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (eventThread == null) {
			throw new RuntimeException(
					"Could not find the EventDispatchThread!");
		}
		eventThread.setAccessible(true);
		try {
			EventDispatchThread actual_thread = (EventDispatchThread) eventThread
					.get(queue);
			actual_thread.addEventFilter(new LardStrainer(this));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Could not acess the EventDispatchThread field!");
		}
	}

	void remakedisplay() {
		if (realApplet.getHeight() == 0 || realApplet.getWidth() == 0) {
			display = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		} else if (display.getWidth() != realApplet.getWidth()
				|| display.getHeight() != realApplet.getHeight()) {
			display = new BufferedImage(realApplet.getWidth(), realApplet
					.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
	}

	@Override
	public boolean isActive() {
		return realApplet.isActive();
	}

	@Override
	public void resize(Dimension d) {
		super.resize(d);
		remakedisplay();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		remakedisplay();
	}

	@Override
	public void update(Graphics g) {
		remakedisplay();
		realApplet.update(display.getGraphics());
		g.drawImage(display, 0, 0, null);
	}

	@Override
	public void paint(Graphics g) {
		remakedisplay();
		realApplet.paint(display.getGraphics());
		g.drawImage(display, 0, 0, null);
	}

	@Override
	public void paintAll(Graphics g) {
		remakedisplay();
		realApplet.paintAll(display.getGraphics());
		g.drawImage(display, 0, 0, null);
	}

	@Override
	public void paintComponents(Graphics g) {
		remakedisplay();
		realApplet.paintComponents(display.getGraphics());
		g.drawImage(display, 0, 0, null);
	}

	@Override
	public String getAppletInfo() {
		return realApplet.getAppletInfo();
	}

	@Override
	public void init() {
		realApplet.init();
	}

	@Override
	public void start() {
		realApplet.start();
	}

	@Override
	public void stop() {
		realApplet.stop();
	}

	@Override
	public void destroy() {
		realApplet.destroy();
	}

	@Override
	public URL getDocumentBase() {
		return realApplet.getDocumentBase();
	}

	@Override
	public URL getCodeBase() {
		return realApplet.getCodeBase();
	}

	@Override
	public void setVisible(boolean b) {
		realApplet.setVisible(b);
		super.setVisible(b);
	}

	public void focusGained(FocusEvent e) {
		for (FocusListener f : realApplet.getFocusListeners()) {
			f.focusGained(e);
		}
	}

	public void focusLost(FocusEvent e) {
		for (FocusListener f : realApplet.getFocusListeners()) {
			f.focusLost(e);
		}
	}

	public void mouseClicked(MouseEvent e) {
		for (MouseListener m : realApplet.getMouseListeners()) {
			m.mouseClicked(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
		for (MouseListener m : realApplet.getMouseListeners()) {
			m.mouseEntered(e);
		}
	}

	public void mouseExited(MouseEvent e) {
		for (MouseListener m : realApplet.getMouseListeners()) {
			m.mouseExited(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		for (MouseListener m : realApplet.getMouseListeners()) {
			m.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		for (MouseListener m : realApplet.getMouseListeners()) {
			m.mouseReleased(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		for (MouseMotionListener m : realApplet.getMouseMotionListeners()) {
			m.mouseDragged(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		for (MouseMotionListener m : realApplet.getMouseMotionListeners()) {
			m.mouseMoved(e);
		}
	}

	public void keyPressed(KeyEvent e) {
		for (KeyListener k : realApplet.getKeyListeners()) {
			k.keyPressed(e);
		}
	}

	public void keyReleased(KeyEvent e) {
		for (KeyListener k : realApplet.getKeyListeners()) {
			k.keyReleased(e);
		}
	}

	public void keyTyped(KeyEvent e) {
		for (KeyListener k : realApplet.getKeyListeners()) {
			k.keyTyped(e);
		}
	}

	public void caretPositionChanged(InputMethodEvent event) {
		for (InputMethodListener m : realApplet.getInputMethodListeners()) {
			m.caretPositionChanged(event);
		}
	}

	public void inputMethodTextChanged(InputMethodEvent event) {
		for (InputMethodListener m : realApplet.getInputMethodListeners()) {
			m.inputMethodTextChanged(event);
		}
	}

}