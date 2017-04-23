package com.js.interpreter.tokens;

import com.js.interpreter.exceptions.BadOperationTypeException;
import com.js.interpreter.pascaltypes.BasicType;
import com.js.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.tokens.Token.precedence;

import javax.naming.OperationNotSupportedException;

public enum OperatorTypes {
    NOT(true) {
        @Override
        public Object operate(boolean b) throws OperationNotSupportedException {
            return !b;
        }

        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return ~l;
        }

        @Override
        public void verifyOperation(DeclaredType type)
                throws BadOperationTypeException {
            if (type != BasicType.Boolean) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Negation;
        }
    },
    MULTIPLY(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 * d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 * l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.Character
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    DIVIDE(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            if (d2 == 0) {
                throw new ArithmeticException("/ by zero");
            }
            return d1 / d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            if (l2 == 0) {
                throw new ArithmeticException("/ by zero");
            }
            return (double) l1 / (double) l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.Character
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

    },
    DIV(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return ((long) d1) / ((long) d2);
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 / l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder
                    || GCF == BasicType.Character) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    MOD(false) {
        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 % l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Integer
                    && GCF != BasicType.Long) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    AND(false) {
        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 && b2;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    PLUS(true) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 + d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 + l2;
        }

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
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }
    },
    MINUS(true) {
        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 - l2;
        }

        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 - d2;
        }

        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return -l;
        }

        @Override
        public Object operate(double d) throws OperationNotSupportedException {
            return -d;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }
    },
    OR(false) {
        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 || b2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Boolean) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }
    },
    XOR(false) {
        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 ^ b2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 ^ l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Boolean) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }
    },
    SHIFTLEFT(false) {
        @Override
        public Object operate(long l1, long l2) {
            return l1 << l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Integer
                    && GCF != BasicType.Long) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    SHIFTRIGHT(false) {
        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 >> l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Integer
                    && GCF != BasicType.Long) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }
    },
    LESSTHAN(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 < d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 < l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    GREATERTHAN(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 > d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 > l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    EQUALS(false) {
        @Override
        public Object operate(char c1, char c2)
                throws OperationNotSupportedException {
            return c1 == c2;
        }

        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 == b2;
        }

        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 == d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 == l2;
        }

        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return s1.equals(s2);
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            return;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    LESSEQ(false) {
        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 <= l2;
        }

        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 <= d2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    GREATEREQ(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 >= d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 >= l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    },
    NOTEQUAL(false) {
        @Override
        public Object operate(char c1, char c2)
                throws OperationNotSupportedException {
            return c1 != c2;
        }

        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 != b2;
        }

        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 != d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 != l2;
        }

        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return !(s1.equals(s2));
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            return;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }
    };

    public boolean can_be_unary;

    OperatorTypes(boolean can_be_unary) {
        this.can_be_unary = can_be_unary;
    }

    public Object operate(char c1, char c2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on character types");
    }

    public Object operate(double d1, double d2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on floating point types");
    }

    public Object operate(long l1, long l2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on integer types");
    }

    public Object operate(boolean b1, boolean b2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on boolean types");

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
                + " does not support operating on an integer type number");

    }

    public void verifyOperation(DeclaredType type1, DeclaredType type2)
            throws BadOperationTypeException {
        DeclaredType GCF = get_GCF(type1, type2);
        if (GCF == null) {
            throw new BadOperationTypeException();
        }
        verifyOperation(GCF);
    }

    void verifyOperation(DeclaredType GCF) throws BadOperationTypeException {
        return;
    }

    public Object operate(Object o) throws OperationNotSupportedException {
        if (o instanceof Boolean) {
            return operate(((Boolean) o).booleanValue());
        }
        if (o instanceof Integer) {
            return ((Long) operate(((Integer) o).longValue())).intValue();
        }
        if (o instanceof Long) {
            return operate(((Long) o).longValue());
        }
        if (o instanceof Double) {
            return operate(((Double) o).doubleValue());
        }
        throw new RuntimeException("unrecognized type " + o.getClass()
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
