package edu.js.interpreter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.js.interpreter.preprocessed.custom_type_generator;
import edu.js.interpreter.preprocessed.plugin_declaration;
import edu.js.interpreter.processing.pascal_plugin;
import edu.js.interpreter.processing.pascal_program;
import edu.js.interpreter.processing.run_mode;

public class ide extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextArea programInput;

	JPanel wholeWindow;

	JPanel buttonsPanel;

	JButton runButton;

	JButton stopButton;

	JButton loadFileButton;

	JButton saveButton;

	JFileChooser fc;

	List<plugin_declaration> plugins;

	JButton clearPluginsButton;

	JButton addPluginsButton;

	pascal_program program;

	custom_type_generator type_generator;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ide();
	}

	void errorReadingFile() {
		JOptionPane.showMessageDialog(this, "Error reading file", "Error",
				JOptionPane.ERROR_MESSAGE);

	}

	void loadFile(File toOpen) {
		try {
			FileReader reader = new FileReader(toOpen);
			char[] buffer = new char[(int) toOpen.length()];
			reader.read(buffer);
			programInput.setText(new String(buffer));
		} catch (IOException e) {
			errorReadingFile();
		}
	}

	void loadFile() {
		int retval = fc.showOpenDialog(this);
		switch (retval) {
		case JFileChooser.APPROVE_OPTION:
			loadFile(fc.getSelectedFile());
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			errorReadingFile();
		}
	}

	public ide() {
		super();
		this.setTitle("pascalinterpreterinjava ide");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 300);
		fc = new JFileChooser(System.getProperty("user.dir"));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		plugins = new ArrayList<plugin_declaration>();
		this.addNewPluginsDirectory(new File(System.getProperty("user.dir")
				+ File.separatorChar + "edu" + File.separatorChar + "js"
				+ File.separatorChar + "interpreter" + File.separatorChar
				+ "plugins" + File.separatorChar));
		wholeWindow = new JPanel();
		this.add(wholeWindow);
		programInput = new JTextArea();
		wholeWindow.setLayout(new BoxLayout(wholeWindow, BoxLayout.Y_AXIS));
		wholeWindow.add(programInput);
		buttonsPanel = new JPanel();
		wholeWindow.add(buttonsPanel);
		runButton = new JButton();
		runButton.setText("Run");
		buttonsPanel.add(runButton);
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						runProgram();
					}
				}).start();
			}
		});
		stopButton = new JButton();
		stopButton.setText("Stop");
		buttonsPanel.add(stopButton);
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopProgram();
			}
		});
		loadFileButton = new JButton();
		loadFileButton.setText("Load Program");
		buttonsPanel.add(loadFileButton);
		loadFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadFile();
			}
		});
		saveButton = new JButton();
		saveButton.setText("Save");
		buttonsPanel.add(saveButton);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		addPluginsButton = new JButton();
		addPluginsButton.setText("Add Plugins Directory");
		buttonsPanel.add(addPluginsButton);
		addPluginsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectPluginsDirectory();
			}
		});

		clearPluginsButton = new JButton();
		clearPluginsButton.setText("Clear Plugins");
		buttonsPanel.add(clearPluginsButton);
		clearPluginsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlugins();
			}
		});
		loadFile(new File(System.getProperty("user.dir") + File.separatorChar
				+ "testprogram.pas"));
		type_generator = new custom_type_generator(new File(System.getProperty("user.dir")));
		setVisible(true);
	}

	void saveFile() {
		int retval = fc.showSaveDialog(this);
		File toSave;
		switch (retval) {
		case JFileChooser.APPROVE_OPTION:
			try {
				toSave = fc.getSelectedFile();
				FileWriter writer = new FileWriter(toSave);
				writer.write(programInput.getText());
				writer.close();
			} catch (IOException e) {
				errorWritingFile();
			}

		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			errorWritingFile();
		}
	}

	void errorWritingFile() {
		JOptionPane.showMessageDialog(this, "Error writing file", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	void clearPlugins() {
		plugins.clear();
	}

	void selectPluginsDirectory() {
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int retval = fc.showOpenDialog(this);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		File pluginFolder;
		switch (retval) {
		case JFileChooser.APPROVE_OPTION:
			pluginFolder = fc.getSelectedFile();
			addNewPluginsDirectory(pluginFolder);
		case JFileChooser.CANCEL_OPTION:
			break;
		case JFileChooser.ERROR_OPTION:
			errorReadingFile();
		}
	}

	@SuppressWarnings("deprecation")
	void addNewPluginsDirectory(File pluginFolder) {
		File[] pluginarray = pluginFolder.listFiles(new java.io.FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".class");
			}
		});
		ClassLoader classloader;
		try {
			classloader = new URLClassLoader(new URL[] { pluginFolder
					.getParentFile().toURL() });
			for (File f : pluginarray) {
				try {
					String filename = f.getName();
					filename = filename
							.substring(0, filename.indexOf(".class"));
					Class c = classloader
							.loadClass("edu.js.interpreter.plugins." + filename);
					if (pascal_plugin.class.isAssignableFrom(c)) {
						for (Method m : c.getMethods()) {
							if (Modifier.isStatic(m.getModifiers())) {
								plugin_declaration tmp = new plugin_declaration(
										m);
								this.plugins.add(tmp);
							}
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	void runProgram() {
		program = new pascal_program(programInput.getText(), plugins,
				type_generator);
		program.run();
	}

	void stopProgram() {
		program.mode = run_mode.stopped;
	}
}
