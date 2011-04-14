package com.js.interpreter.ast.instructions.case_statement;

import java.util.ArrayList;
import java.util.List;

import com.js.interpreter.ast.ExpressionContext;
import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.instructions.returnsvalue.ReturnsValue;
import com.js.interpreter.exceptions.ExpectedTokenException;
import com.js.interpreter.exceptions.ParsingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.basic.ColonToken;
import com.js.interpreter.tokens.basic.CommaToken;
import com.js.interpreter.tokens.basic.DotDotToken;
import com.js.interpreter.tokens.basic.ElseToken;
import com.js.interpreter.tokens.basic.OfToken;
import com.js.interpreter.tokens.basic.PeriodToken;
import com.js.interpreter.tokens.basic.SemicolonToken;
import com.js.interpreter.tokens.grouping.CaseToken;

public class CaseInstruction extends DebuggableExecutable {
	ReturnsValue switch_value;
	CasePossibility[] possibilies;
	InstructionGrouper otherwise;
	LineInfo line;

	public CaseInstruction(CaseToken i, ExpressionContext context)
			throws ParsingException {
		this.line = i.lineInfo;
		switch_value = i.getNextExpression(context);
		Token next = i.take();
		if (!(next instanceof OfToken)) {
			throw new ExpectedTokenException("of", next);
		}
		List<CasePossibility> possibilities = new ArrayList<CasePossibility>();
		while (!(i.peek() instanceof ElseToken)
				&& !(i.peek() instanceof EOF_Token)) {
			List<CaseCondition> conditions = new ArrayList<CaseCondition>();
			while (true) {
				ReturnsValue val = i.getNextExpression(context);
				if (i.peek() instanceof DotDotToken) {
					i.take();
					ReturnsValue upper = i.getNextExpression(context);
					conditions.add(new RangeOfValues(val, upper));
				} else {
					conditions.add(new SingleValue(val));
				}
				if (i.peek() instanceof CommaToken) {
					i.take();
					continue;
				} else if (i.peek() instanceof ColonToken) {
					i.take();
					break;
				} else {
					throw new ExpectedTokenException("[comma or colon]",
							i.take());
				}
			}
			Executable command = i.get_next_command(context);
			i.assert_next_semicolon();
			possibilities.add(new CasePossibility(conditions
					.toArray(new CaseCondition[conditions.size()]), command));
		}
		otherwise = new InstructionGrouper(i.peek().lineInfo);
		if (i.peek() instanceof ElseToken) {
			i.take();
			while (i.hasNext()) {
				otherwise.add_command(i.get_next_command(context));
				i.assert_next_semicolon();
			}
		}
		this.possibilies = possibilities
				.toArray(new CasePossibility[possibilities.size()]);
	}

	@Override
	public ExecutionResult executeImpl(VariableContext f,
			RuntimeExecutable<?> main) throws RuntimePascalException {
		Object value = switch_value.getValue(f, main);
		for (int i = 0; i < possibilies.length; i++) {
			for (int j = 0; j < possibilies[i].conditions.length; j++) {
				if (possibilies[i].conditions[j].fits(main, f, value)) {
					return possibilies[i].execute(f, main);
				}
			}
		}
		return otherwise.execute(f, main);
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}
}
