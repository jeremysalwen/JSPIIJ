package com.js.interpreter.startup;

import java.io.Reader;

public interface ScriptSource {
public String[] list();
public Reader read(String scriptname);
}
