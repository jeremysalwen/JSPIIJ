package edu.js.interpreter.plugins;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import edu.js.interpreter.gui.IDE;
import edu.js.interpreter.preprocessed.interpretingobjects.Pointer;
import edu.js.interpreter.processing.PascalPlugin;

public class SCAR_Networking implements PascalPlugin {
	IDE ide;

	Map<Integer, spedClient> clients = new HashMap<Integer, spedClient>();

	Map<Integer, Socket> connections = new HashMap<Integer, Socket>();

	int clientnumber = 0;

	int connectionnumber = 0;

	public SCAR_Networking(IDE i) {
		this.ide = i;
	}

	public void openwebpage(String URL) {
		if (ide.settings.hasURLAcess(URL)) {
			try {
				Desktop.getDesktop().browse(new java.net.URI(URL));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	public String getpage(String url) {
		if (ide.settings.hasURLAcess(url)) {
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
				if (i.read(buffer) == -1) { // TODO does not handle gzipped
					// content
					return "";
				}
				return new String(buffer);
			} catch (Exception e) {
			}
		}
		return "";
	}

	public int InitializeHttpClient(boolean handlecookies,
			boolean handleredirects) {
		HttpClientParams params = new HttpClientParams();
		params
				.setCookiePolicy(handlecookies ? (CookiePolicy.BROWSER_COMPATIBILITY)
						: (CookiePolicy.IGNORE_COOKIES));
		params.setParameter("http.protocol.handle-redirects",
				handleredirects ? "True" : "False");
		HttpClient client = new HttpClient(params);
		clients.put(clientnumber, new spedClient(client));
		return clientnumber++;
	}

	public void freehttpclient(int number) {
		clients.get(number).post.releaseConnection();
		clients.remove(number);
	}

	public String getHttpPage(int clientnum, String page) {
		try {
			spedClient client = clients.get(clientnum);
			HttpMethod m = new GetMethod(page);
			m.setParams(client.client.getParams());
			int status_code = client.client.executeMethod(m);
			if (status_code == HttpStatus.SC_OK) {
				return m.getResponseBodyAsString();
			}
		} catch (Exception e) {
		}
		return "";
	}

	public void setHttpUserAgent(int clientnum, String user_agent) {
		this.clients.get(clientnum).client.getParams().setParameter(
				"User-Agent", user_agent);
	}

	public void addpostvariable(int clientnum, String varname, String varvalue) {
		spedClient client = clients.get(clientnum);
		client.post.addParameter(varname, varvalue);
	}

	public void clearPostData(int clientnum) {
		spedClient c = clients.get(clientnum);
		c.post.releaseConnection();
		c.post = new PostMethod();
		c.post.setParams(c.client.getParams());
	}

	public String posthttppageex(int clientnum, String url) {
		try {
			spedClient c = clients.get(clientnum);
			c.post.setURI(new URI(url));
			if (c.client.executeMethod(c.post) == HttpStatus.SC_OK) {
				return c.post.getResponseBodyAsString();
			}
		} catch (Exception e) {
		}
		return "";
	}

	public String getRawHeaders(int clientnum) {
		spedClient c = clients.get(clientnum);
		StringBuilder b = new StringBuilder();
		Header[] headers = c.post.getResponseHeaders();
		for (Header h : headers) {
			b.append(h.toExternalForm());
		}
		return b.toString();
	}

	class spedClient {
		public spedClient(HttpClient c) {
			this.client = c;
			post = new PostMethod();
			post.setParams(c.getParams());
		}

		public HttpClient client;

		public PostMethod post;
	}

	public int openConnection(String host, int port, int timeout) {
		try {
			Socket s = new Socket(host, port);
			s.setSoTimeout(timeout);
			connections.put(connectionnumber, s);
			return connectionnumber++;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean readConnectionData(int connection, Pointer<String> buffer) {
		try {
			Socket s = connections.get(connection);

			InputStream stream = s.getInputStream();
			byte[] buff = new byte[stream.available()];
			stream.read(buff);
			buffer.set(new String(buff));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean sendConnectionData(int connection, String tosend) {
		try {
			Socket s = connections.get(connection);
			s.getOutputStream().write(tosend.getBytes());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void freeConnection(int i) {
		try {
			connections.get(i).close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		connections.remove(i);

	}

	public boolean isconnectionopen(int i) {
		try {
			return connections.get(i).isConnected();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
