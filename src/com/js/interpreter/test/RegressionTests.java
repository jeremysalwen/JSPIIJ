package com.js.interpreter.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.startup.Interface;
import com.js.interpreter.startup.ScriptSource;

public class RegressionTests {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HashMap<String, Object> pluginargs = new HashMap<String, Object>();
        pluginargs.put("stdout", new PrintStream(os));
        ArrayList<ClassLoader> pluginsources = new ArrayList<ClassLoader>();
        pluginsources.add(Thread.currentThread().getContextClassLoader());
        File goodtests = new File("tests/good/");
        for (File f : goodtests.listFiles()) {
            boolean pass = false;
            try {
                Interface.executeScript(f.getName(), new FileReader(f),
                        pluginsources, new ArrayList<ScriptSource>(),
                        new ArrayList<ScriptSource>(), pluginargs);
                os.reset();
                pass = true;
            } catch (FileNotFoundException e) {
                System.out
                        .println("[G][FAIL] Error, File not found in regression test "
                                + f.getName() + ": " + e);
            } catch (ParsingException e) {
                System.out.println("[G][FAIL] Failure parsing test "
                        + f.getName() + ": " + e);
            } catch (RuntimePascalException e) {
                System.out.println("[G][FAIL] Failure executing test "
                        + f.getName() + ": " + e);
            }
            if (pass) {
                System.out.println("[G][SUCC] Success executing test "
                        + f.getName());
            }
        }
        File badtests = new File("tests/bad/");
        for (File f : badtests.listFiles()) {
            boolean pass = false;
            try {
                Interface.executeScript(f.getName(), new FileReader(f),
                        pluginsources, new ArrayList<ScriptSource>(),
                        new ArrayList<ScriptSource>(), pluginargs);
                os.reset();
                pass = true;
            } catch (FileNotFoundException e) {
                System.out
                        .println("[B][FAIL] Error, File not found in regression test "
                                + f.getName() + ": " + e);
            } catch (ParsingException e) {
                System.out.println("[B][SUCC] Did not compile " + f.getName()
                        + ": " + e);
            } catch (RuntimePascalException e) {
                System.out
                        .println("[B][FAIL] Compiled but did not execute test "
                                + f.getName() + ": " + e);
            }
            if (pass) {
                System.out
                        .println("[B][FAIL] Executed test it should have failed to "
                                + f.getName());
            }
        }
        File outputtests = new File("tests/compare/");
        for (File f : outputtests.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pas");
            }
        })) {
            boolean pass = false;
            try {
                Interface.executeScript(f.getName(), new FileReader(f),
                        pluginsources, new ArrayList<ScriptSource>(),
                        new ArrayList<ScriptSource>(), pluginargs);

                pass = true;
            } catch (FileNotFoundException e) {
                System.out
                        .println("[O][FAIL] Error, File not found in regression test "
                                + f.getName() + ": " + e);
            } catch (ParsingException e) {
                System.out.println("[O][FAIL] Did not compile " + f.getName()
                        + ": " + e);
            } catch (RuntimePascalException e) {
                System.out
                        .println("[O][FAIL] Compiled but did not execute test "
                                + f.getName() + ": " + e);
            }
            if (pass) {
                String output = os.toString();
                Scanner s;
                try {
                    s = new Scanner(new File(outputtests, f.getName()
                            .replaceFirst("\\.pas", ".out")));

                    s.useDelimiter("\0");
                    String goal = s.next();
                    if (goal.equals(output)) {
                        System.out
                                .println("[O][SUCC] Test gave correct output: "
                                        + f.getName());
                    } else {
                        System.out
                                .println("[O][FAIL] Test gave incorrect output: "
                                        + f.getName());
                        System.out.println(output);
                    }
                } catch (FileNotFoundException e) {
                    System.out
                            .println("[O][FAIL] Could not find target output file for: "
                                    + f.getName());
                }
            }
            os.reset();
        }
    }
}
