package edu.js.interpreter.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.io.File;
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

	public static void main(String[] args) {
		JDialog askPermissionDialog;
		JLabel filenamelabel = new JLabel();
		filenamelabel.setText("/tmp/blah");
		askPermissionDialog = new JDialog();
		JPanel contents = new JPanel();
		JPanel info = new JPanel();
		askPermissionDialog.add(info);
		info.setLayout(new BoxLayout(info, BoxLayout.X_AXIS));
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		info.add(new JLabel(FileSystemView.getFileSystemView().getSystemIcon(
				new File("/home/jeremy/lard.txt"))));
		JPanel text = new JPanel();
		text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
		text.add(new JLabel("Script is trying to acess the following file:"));
		text.add(filenamelabel);
		text.add(new JLabel("Do you want to permit or deny this operation?"));
		info.add(text);
		contents.add(info);
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
		ButtonGroup options = new ButtonGroup();
		JRadioButton deny = new JRadioButton("Deny access to the file above");
		options.add(deny);
		JRadioButton once = new JRadioButton("Permit once for this file");
		options.add(once);
		JRadioButton always = new JRadioButton("Always permit for this file");
		options.add(always);
		JRadioButton directory = new JRadioButton();
		options.add(directory);
		deny.setSelected(true);
		buttons.add(deny);
		buttons.add(once);
		buttons.add(always);
		buttons.add(directory);
		contents.add(buttons);
		JPanel okcancel = new JPanel();
		okcancel.setLayout(new BoxLayout(okcancel, BoxLayout.X_AXIS));
		okcancel.add(new JButton("Ok"));
		okcancel.add(new JButton("Cancel"));
		contents.add(okcancel);
		askPermissionDialog.add(contents);
		askPermissionDialog.pack();
		askPermissionDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		askPermissionDialog.setVisible(true);
	}

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
