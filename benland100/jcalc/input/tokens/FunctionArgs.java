package benland100.jcalc.input.tokens;

import java.util.ArrayList;

import benland100.jcalc.input.Token;

import preProcessing.PascalProgram;
import preProcessing.ReturningProcessedObject;
import preProcessing.PreProcessedOnly.PreprocessedFunction;
import processing.StaticMethods;

public class FunctionArgs extends Token {

	String data;

	ArrayList<ReturningProcessedObject> args;

	private PreprocessedFunction p;

	public FunctionArgs(String data, PreprocessedFunction p) {
		this.p = p;
		ArrayList<ReturningProcessedObject> args = preProcessArgs(data);
		this.args = args;
		this.data = data;
	}

	public ArrayList<ReturningProcessedObject> getArgs() {
		return args;
	}

	public String toString() {
		return data;
	}

	public ArrayList<ReturningProcessedObject> preProcessArgs(String data) {
		ArrayList<ReturningProcessedObject> result = new ArrayList<ReturningProcessedObject>();
		if (!(data.contains("(") && data.contains(")")))
			return result;
		String theString = data.substring(1, data.length());
		String[] split = { "," };
		String[] argSplit = StaticMethods.splitNoQuotes(theString, split);
		for (String s : argSplit) {
			s = s.trim().toLowerCase();
			result.add(p.getExpressionPreprocessed(s));
		}
		return result;
	}
}
