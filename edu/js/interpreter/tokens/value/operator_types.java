package edu.js.interpreter.tokens.value;

import javax.naming.OperationNotSupportedException;

public enum operator_types {
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
	},
	DIVIDE(false) {
		@Override
		public Object operate(double d1, double d2)
				throws OperationNotSupportedException {
			return d1 / d2;
		}

		@Override
		public Object operate(long l1, long l2)
				throws OperationNotSupportedException {
			return l1 / l2;
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
			return s1 + s2;
		}
	},
	MINUS(true) {
		@Override
		public Object operate(long l1, long l2)
				throws OperationNotSupportedException {
			return l1 - l1;
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
	},
	NOT(true) {
		@Override
		public Object operate(boolean b) throws OperationNotSupportedException {
			return !b;
		}

		@Override
		public Object operate(long l) throws OperationNotSupportedException {
			return ~l;
		}
	},
	AND(false) {
		@Override
		public Object operate(boolean b1, boolean b2)
				throws OperationNotSupportedException {
			return b1 && b2;
		}
	},
	OR(false) {
		@Override
		public Object operate(boolean b1, boolean b2)
				throws OperationNotSupportedException {
			return b1 || b2;
		}

		@Override
		public Object operate(long l1, long l2)
				throws OperationNotSupportedException {
			return l1 | l2;
		}
	},
	XOR(false) {
		@Override
		public Object operate(boolean b1, boolean b2)
				throws OperationNotSupportedException {
			return b1 ^ b2;
		}

		public Object operate(long l1, long l2)
				throws OperationNotSupportedException {
			return l1 ^ l2;
		}
	},
	SHIFTLEFT(false) {
		@Override
		public Object operate(long l1, long l2) {
			return l1 << l2;// TODO should it be signed or unsigned shift?
		}
	},
	SHIFTRIGHT(false) {
		@Override
		public Object operate(long l1, long l2)
				throws OperationNotSupportedException {
			return l1 >> l2;
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
	},
	EQUALS(false) {
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
	},
	NOTEQUAL(false) {
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
	},
	MOD(false) {
		@Override
		public Object operate(long l1, long l2)
				throws OperationNotSupportedException {
			return l1 % l2;
		}
	};

	public boolean can_be_unary;

	operator_types(boolean can_be_unary) {
		this.can_be_unary = can_be_unary;
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

	public Object operate(Object o) {
		if (o instanceof Boolean) {
			return operate((Boolean) o);
		}
		if (o instanceof Long) {
			return operate((Long) o);
		}
		if (o instanceof Double) {
			return operate((Double) o);
		}
		throw new RuntimeException("unrecognized type " + o.getClass()
				+ " for operation " + this);
	}

	public Object operate(String s1, String s2)
			throws OperationNotSupportedException {
		throw new OperationNotSupportedException(this
				+ " does not support operating on Strings");

	}
}