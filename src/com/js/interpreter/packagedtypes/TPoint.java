package com.js.interpreter.packagedtypes;

import com.js.interpreter.runtime.variables.ContainsVariables;

public class TPoint implements ContainsVariables {
    public int x;

    public int y;

    public TPoint() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public Object get_var(String name) {
        if (name.equals("x")) {
            return x;
        }
        if (name.equals("y")) {
            return y;
        }
        return null;
    }

    @Override
    public void set_var(String name, Object val) {
        if (name.equals("x")) {
            x = (Integer) val;
        }
        if (name.equals("y")) {
            y = (Integer) val;
        }
        System.err.println("Tried to put to unexistant struct member");
    }

    @Override
    public ContainsVariables clone() {
        TPoint result = new TPoint();
        result.x = this.x;
        result.y = this.y;
        return result;
    }
}
