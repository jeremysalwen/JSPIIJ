package edu.js.interpreter.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileSystemView;

public class security_settings {
	ide ide;

	JDialog askPermissionDialog;

	JLabel filenamelabel;

	public security_settings(ide i) {
		this.ide = i;
		askPermissionDialog = new JDialog(ide);
		askPermissionDialog.add(new JLabel(
				"Script is trying to acess the following file:"));
		filenamelabel = new JLabel();
		askPermissionDialog.add(filenamelabel);
		askPermissionDialog.setVisible(true);
	}

	Set<File> allowed_files = new HashSet<File>();

	Set<File> allowed_directories = new HashSet<File>();

	Set<URL> allowed_URLs = new HashSet<URL>();

	Set<URL> allowed_domains = new HashSet<URL>();

	public boolean hasAccess(String location) {
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
}
