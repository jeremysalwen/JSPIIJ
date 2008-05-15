package preprocessed.instructions;

import java.util.LinkedList;
import java.util.Vector;

import preprocessed.interpreting_objects.function_on_stack;

public class instruction_grouper implements executable {
	LinkedList<executable> instructions;
	
	public instruction_grouper(LinkedList<executable> instructions) {
		this.instructions=instructions;
	}
	public instruction_grouper(Iterable<executable> instructions) {
		this.instructions=new LinkedList<executable>();
		for(executable e:instructions) {
			this.instructions.add(e);
		}
	}
	public instruction_grouper(executable[] instructions) {
		this.instructions=new LinkedList<executable>();
		for(executable e:instructions) {
			this.instructions.add(e);
		}
	}
	public void execute(function_on_stack f) {
		for(executable e:instructions) {
			e.execute(f);
		}
	}

}
