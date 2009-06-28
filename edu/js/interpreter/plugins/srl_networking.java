package edu.js.interpreter.plugins;

import java.awt.Desktop;
import java.awt.Robot;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import edu.js.interpreter.gui.ide;
import edu.js.interpreter.processing.pascal_plugin;

public class srl_networking implements pascal_plugin {
	ide ide;

	Map<Integer, spedClient> clients = new HashMap<Integer, spedClient>();

	int clientnumber = 0;

	public srl_networking(ide i) {
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
}
