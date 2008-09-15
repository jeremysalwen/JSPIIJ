package preprocessed;

import processing.pascal_program;

public abstract class abstract_function {

	abstract String get_name();

	abstract Class[] get_arg_types();

	public abstract Class get_return_type();

	public int hashCode() {
		int hashcode = get_name().hashCode();
		Class[] types = get_arg_types();
		for (int i = 0; i < types.length; i++) {
			hashcode ^= types[i].hashCode();
		}
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof abstract_function) {
			abstract_function other = (abstract_function) obj;
			if(get_name().equals(other.get_name())) {
				Class[] types=get_arg_types();
				Class[] other_types=other.get_arg_types();
				if(types.length==other_types.length) {
					for(int i=0; i<types.length; i++) {
						if(types[i]!=other_types[i]) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}
/**
 * This invokes a function call of any type.
 * 
 * @param program The program context.
 * @param arguments 
 * @return The return value of the called function.
 */
	public abstract Object call(pascal_program program, Object[] arguments);
}
