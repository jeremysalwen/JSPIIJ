package com.js.interpreter.pascaltypes;

public class SubrangeType {
	public SubrangeType() {
		this.lower = 0;
		this.size = 0;
	}

	public SubrangeType(int lower, int size) {
		this.lower = lower;
		this.size = size;
	}

	int lower;
	int size;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lower;
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SubrangeType))
			return false;
		SubrangeType other = (SubrangeType) obj;
		return lower == other.lower && size == other.size;
	}
	public boolean contains(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SubrangeType))
			return false;
		SubrangeType other = (SubrangeType) obj;
		return lower <= other.lower && (lower+size) >= (other.lower+other.size);
	}
	@Override
	public String toString() {
		return lower + ".." + (lower + size - 1);
	}
}
