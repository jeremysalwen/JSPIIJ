package edu.js.interpreter.gui;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.js.SCARlib.SCARLibDummy;
import edu.js.SCARlib.SCARLibInterface;
import edu.js.interpreter.preprocessed.CustomTypeGenerator;
import edu.js.interpreter.preprocessed.PluginDeclaration;
import edu.js.interpreter.processing.PascalPlugin;
import edu.js.interpreter.processing.PascalProgram;
import edu.js.interpreter.processing.RunMode;

public class IDE extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String pluginsPackage = "edu.js.interpreter.plugins";
	JEditorPane programInput;

	public JTextArea debugBox;

	JPanel wholeWindow;

	JPanel buttonsPanel;

	JButton runButton;

	JButton pauseButton;

	JButton stopButton;

	JButton loadFileButton;

	JButton saveButton;

	JButton clearDebugButton;

	JFileChooser fc;

	List<PluginDeclaration> plugins;

	JButton clearPluginsButton;

	JButton addPluginsButton;

	public JLabel status_bar;

	PascalProgram program;

	CustomTypeGenerator type_generator;

	public SecuritySettings settings;

	public Applet client = null;

	/*
	 * This is actually a native window, or a pointer to one.
	 */
	public long window;

	public SCARLibInterface connection;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new IDE();
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

	public IDE() {
		super();
		settings = new SecuritySettings(this);
		this.setTitle("pascalinterpreterinjava IDE");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		fc = new JFileChooser(System.getProperty("user.dir"));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		plugins = new ArrayList<PluginDeclaration>();
		this.addNewPluginsDirectory(new File(System.getProperty("user.dir")
				+ File.separatorChar + "bin" + File.separatorChar + "edu"
				+ File.separatorChar + "js" + File.separatorChar
				+ "interpreter" + File.separatorChar + "plugins"
				+ File.separatorChar));
		wholeWindow = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(wholeWindow, BorderLayout.NORTH);
		this.status_bar = new JLabel("Status bar");
		this.add(status_bar, BorderLayout.SOUTH);
		programInput = new JEditorPane();
		debugBox = new JTextArea("Debug Box\n");
		wholeWindow.setLayout(new BoxLayout(wholeWindow, BoxLayout.Y_AXIS));
		wholeWindow.add(new JScrollPane(programInput));
		buttonsPanel = new JPanel();
		wholeWindow.add(buttonsPanel);
		wholeWindow.add(new JScrollPane(debugBox));
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
		pauseButton = new JButton();
		pauseButton.setText("Pause");
		buttonsPanel.add(pauseButton);
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pauseProgram();
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
		clearDebugButton = new JButton();
		clearDebugButton.setText("Clear Debug");
		buttonsPanel.add(clearDebugButton);
		clearDebugButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearDebug();
			}
		});
		loadFile(new File(System.getProperty("user.dir") + File.separatorChar
				+ "testprogram.pas"));
		type_generator = new CustomTypeGenerator(new File(System
				.getProperty("user.dir")));
		connection = new SCARLibDummy();
		window = connection.getRootWindow();
		pack();
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

	void addPluginClassCheck(Class c) {
		if (PascalPlugin.class.isAssignableFrom(c)) {
			Object o;
			try {
				Constructor constructor = c.getConstructor(IDE.class);
				o = constructor.newInstance(this);
			} catch (NoSuchMethodException e) {
				o = null;
			} catch (IllegalArgumentException e) {
				o = null;
			} catch (InstantiationException e) {
				o = null;
			} catch (IllegalAccessException e) {
				o = null;
			} catch (InvocationTargetException e) {
				o = null;
			}
			for (Method m : c.getDeclaredMethods()) {
				if (Modifier.isPublic(m.getModifiers())) {
					PluginDeclaration tmp = new PluginDeclaration(o, m);
					this.plugins.add(tmp);
				}
			}
		}
	}

	void addNewPluginsDirectory(File pluginFolder) {
		if (pluginFolder.isFile()) {
			JarFile jar;
			try {
				jar = new JarFile(pluginFolder);

				for (Enumeration<JarEntry> e = jar.entries(); e
						.hasMoreElements();) {
					JarEntry current = e.nextElement();
					if (current.getName().length() > pluginsPackage.length()
							&& current.getName().startsWith(pluginsPackage)
							&& current.getName().endsWith(".class")) {
						try {
							Class plugin_class = Class.forName(current
									.getName().replaceAll("/", ".").replace(
											".class", ""));
							addPluginClassCheck(plugin_class);
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}
					return;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (pluginFolder.isDirectory()) {
			File[] pluginarray = pluginFolder
					.listFiles(new java.io.FileFilter() {
						public boolean accept(File pathname) {
							return pathname.getName().endsWith(".class");
						}
					});
			ClassLoader classloader;
			try {
				classloader = new URLClassLoader(new URL[] { pluginFolder
						.toURI().toURL() });

				for (File f : pluginarray) {
					try {
						String filename = f.getName();
						filename = filename.substring(0, filename
								.indexOf(".class"));
						Class c = classloader.loadClass(pluginsPackage + '.'
								+ filename);
						addPluginClassCheck(c);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void runProgram() {
		if (program != null) {
			resumeProgram();
		} else {
			program = new PascalProgram(programInput.getText(), plugins,
					type_generator);
			program.run();
		}
	}

	public void stopProgram() {
		if (program != null) {
			program.mode = RunMode.stopped;
			this.program = null;
		}
	}

	public void pauseProgram() {
		if (program != null) {
			program.mode = RunMode.paused;
		}
	}

	public void resumeProgram() {
		program.mode = RunMode.running;
		program.notifyAll();
	}

	public void output_to_debug(String s) {
		debugBox.append(s);
	}

	public void clearDebug() {
		debugBox.setText("");
	}

}
