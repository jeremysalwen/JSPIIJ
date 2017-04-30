package com.js.interpreter.tokens;

import com.js.interpreter.exceptions.BadOperationTypeException;
import com.js.interpreter.pascaltypes.BasicType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.tokens.Token.precedence;

import javax.naming.OperationNotSupportedException;

public enum OperatorTypes {
    NOT(true, false) {
        @Override
        public Object operate(boolean b) throws OperationNotSupportedException {
            return !b;
        }

        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return ~l;
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType type) {
            return type == BasicType.Boolean;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Negation;
        }
    },
    MULTIPLY(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.Character
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    DIVIDE(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.Character
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

    },
    DIV(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder
                    || GCF == BasicType.Character);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    MOD(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return ! (GCF != BasicType.Integer
                    && GCF != BasicType.Long);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    AND(false, false) {
        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    PLUS(true, false) {
        @Override
        public Object operate(double d) throws OperationNotSupportedException {
            return d;
        }

        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return l;
        }

        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return new StringBuilder(s1).append(s2);
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return ! (GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }
    },
    MINUS(true, false) {
        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return -l;
        }

        @Override
        public Object operate(double d) throws OperationNotSupportedException {
            return -d;
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return ! (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }
    },
    OR(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return  (GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }
    },
    XOR(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return  (GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }
    },
    SHIFTLEFT(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return  (GCF == BasicType.Integer
                    || GCF == BasicType.Long);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    SHIFTRIGHT(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF == BasicType.Integer
                    || GCF != BasicType.Long);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    LESSTHAN(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    GREATERTHAN(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    EQUALS(false, false) {
        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return s1.equals(s2);
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return true;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    LESSEQ(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    GREATEREQ(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return ! (GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    NOTEQUAL(false, false) {
        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return !(s1.equals(s2));
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    ADDRESS(true, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return true;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Dereferencing;
        }
    },
    DEREF(true, true) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return true;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Dereferencing;
        }
    };

    public boolean can_be_unary;
    public boolean postfix;

    OperatorTypes(boolean can_be_unary, boolean postfix) {
        this.can_be_unary = can_be_unary;
        this.postfix = postfix;
    }

    public Object operate(boolean b) throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on a boolean");

    }

    public Object operate(double d) throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on a floating point number");

    }

    public Object operate(long l) throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on an integer operator number");

    }

    public boolean verifyBinaryOperation(DeclaredType type1, DeclaredType type2)
            throws BadOperationTypeException {
        DeclaredType GCF = get_GCF(type1, type2);
        if (GCF == null) {
            return false;
        }
        return verifyBinaryOperation(GCF);
    }

    boolean verifyBinaryOperation(DeclaredType GCF) {
        return true;
    }

    public boolean verifyUnaryOperation(DeclaredType type) {
        return true;
    }

    public Object operate(Object o, RuntimeType type) throws OperationNotSupportedException {
        if (type.declType == BasicType.Boolean) {
            return operate((Boolean) o);
        }
        if (type.declType == BasicType.Integer) {
            return ((Long) operate(((Integer) o).longValue())).intValue();
        }
        if (type.declType == BasicType.Long) {
            return operate((Long) o);
        }
        if (type.declType == BasicType.Double) {
            return operate((Double) o);
        }
        throw new RuntimeException("unrecognized operator " + o.getClass()
                + " for operation " + this);
    }

    public Object operate(String s1, String s2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on Strings");

    }

    public static DeclaredType get_GCF(DeclaredType one, DeclaredType two) {
        if (one == BasicType.StringBuilder
                || two == BasicType.StringBuilder) {
            return BasicType.StringBuilder;
        }
        if (one == BasicType.Double
                || two == BasicType.Double) {
            if (one == BasicType.Boolean
                    || two == BasicType.Boolean) {
                return null;
            }
            return BasicType.Double;
        }
        if (one == BasicType.Long || two == BasicType.Long) {
            if (one == BasicType.Boolean
                    || two == BasicType.Boolean) {
                return null;
            }
            return BasicType.Long;
        }
        if (one == BasicType.Integer
                || two == BasicType.Integer) {
            if (one == BasicType.Boolean
                    || two == BasicType.Boolean) {
                return null;
            }
            return BasicType.Integer;
        }
        if (one == BasicType.Boolean
                && two == BasicType.Boolean) {
            return BasicType.Boolean;
        }
        if (one == BasicType.Character
                && two == BasicType.Character) {
            return BasicType.Character;
        }
        return null;
    }

    public precedence getPrecedence() {
        return null;
    }

}
