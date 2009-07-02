package edu.js.appletloader;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class appletStub implements java.applet.AppletStub {
	public boolean active = false;

	private HashMap<String, String> params;

	private URL documentbase;

	private URL codebase;

	public appletStub(String documentBase, String codebase,
			HashMap<String, String> params) throws MalformedURLException {
		this.params = params;
		this.documentbase = new URL(documentBase);
		this.codebase = new URL(codebase);
	}

	public boolean isActive() {
		return active;
	}

	public URL getDocumentBase() {
		return documentbase;
	}

	public URL getCodeBase() {
		return codebase;
	}

	public String getParameter(String name) {
		String value = params.get(name);
		return value == null ? "" : value;
	}

	public AppletContext getAppletContext() {
		return null;
	}

	public void appletResize(int i, int i1) {
	}

}
