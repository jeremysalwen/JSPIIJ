package edu.js.interpreter.plugins;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import edu.js.interpreter.processing.pascal_plugin;

public class srl_networking implements pascal_plugin {
	
	public static void main(String[] args) {
		openwebpage("http://www.google.com");
	}

	public static void openwebpage(String URL) {
		try {
			((BasicService) ServiceManager.lookup("javax.jnlp.BasicService"))
					.showDocument(new java.net.URL(URL));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnavailableServiceException e) {
			e.printStackTrace();
		}
	}

	public static String getpage(String url) {
		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u
					.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("User-agent",
					"pascalinterpreterinjava browser");
			connection.connect();
			Object content = connection.getContent();
			InputStream i = (InputStream) content;
			int size = connection.getContentLength();
			byte[] buffer = new byte[size];
			if (i.read(buffer) == -1) { // TODO does not handle gzipped content
				return "";
			}
			return new String(buffer);
		} catch (Exception e) {
			return "";
		}
	}
}
