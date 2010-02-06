package edu.js.interpreter.gui;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SecuritySettings {
	Ide ide;

	JDialog askPermissionDialog;

	JLabel filenamelabel;

	public SecuritySettings(Ide i) {
		this.ide = i;
	}

	Set<File> allowed_files = new HashSet<File>();

	Set<File> allowed_directories = new HashSet<File>();

	Set<URL> allowed_URLs = new HashSet<URL>();

	Set<String> allowed_domains = new HashSet<String>();

	public boolean hasFileAccess(String location) {
		File f = new File(location);
		if (allowed_files.contains(f)) {
			return true;
		}
		for (File a : allowed_directories) {
			if (f.getParentFile().equals(a)) {
				return true;
			}
		}
		String deny = "Deny access to the file above";
		String permit = "Permit once for this file";
		String always = "Always permit for this file";
		String folder = "Always permit for this folder";
		Object selectedoption = JOptionPane.showInputDialog(ide,
				"Script is trying to acess the following file:\n" + location
						+ "\nDo you want to permit or deny this operation?",
				"File Access", JOptionPane.OK_CANCEL_OPTION, null,
				new Object[] { deny, permit, always, folder }, deny);
		if (selectedoption.equals(deny)) {
			return false;
		}
		if (selectedoption.equals(permit)) {
			return true;
		}
		if (selectedoption.equals(always)) {
			allowed_files.add(f);
			return true;
		}
		if (selectedoption.equals(folder)) {
			allowed_directories.add(f.getParentFile());
			return true;
		}
		return false;
	}

	public boolean hasURLAcess(String url) {
		try {
			URL u = new URL(url);
			if (allowed_URLs.contains(url)) {
				return true;
			}
			for (String s : allowed_domains) {
				if (s.equals(u.getHost())) {
					return true;
				}
			}
			String deny = "Deny access to the address above";
			String permit = "Permit once for this address";
			String always = "Always permit for this address";
			String domain = "Always permit for this domain";
			Object selectedoption = JOptionPane
					.showInputDialog(
							ide,
							"Script is trying to acess the following file:\n"
									+ url
									+ "\nDo you want to permit or deny this operation?",
							"File Access", JOptionPane.OK_CANCEL_OPTION, null,
							new Object[] { deny, permit, always, domain }, deny);
			if (selectedoption.equals(deny)) {
				return false;
			}
			if (selectedoption.equals(permit)) {
				return true;
			}
			if (selectedoption.equals(always)) {
				allowed_URLs.add(u);
				return true;
			}
			if (selectedoption.equals(domain)) {
				allowed_domains.add(u.getHost());
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			URL u = new URL("http://www.google.com/");
			System.out.println(u.getHost());
			System.out.println(u.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
