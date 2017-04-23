package com.js.interpreter.tokenizer;

import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.interpreter.exceptions.grouping.GroupingException;
import com.js.interpreter.linenumber.LineInfo;
import com.js.interpreter.startup.ScriptSource;
import com.js.interpreter.tokens.EOF_Token;
import com.js.interpreter.tokens.GroupingExceptionToken;
import com.js.interpreter.tokens.Token;
import com.js.interpreter.tokens.WarningToken;
import com.js.interpreter.tokens.closing.ClosingToken;
import com.js.interpreter.tokens.grouping.BaseGrouperToken;
import com.js.interpreter.tokens.grouping.GrouperToken;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Stack;

public class NewLexer implements Runnable {
    public BaseGrouperToken token_queue;
    Stack<GrouperToken> groupers;
    Lexer lexer;

    public NewLexer(Reader reader, String sourcename,
                    List<ScriptSource> searchDirectories) throws GroupingException {
        this.lexer = new Lexer(reader, sourcename, searchDirectories);
        groupers = new Stack<GrouperToken>();
        token_queue = new BaseGrouperToken(new LineInfo(0, sourcename));
        groupers.push(token_queue);
    }

    void TossException(GroupingException e) {
        GroupingExceptionToken t = new GroupingExceptionToken(e);
        for (GrouperToken g : groupers) {
            g.put(t);
        }
    }

    void TossException(LineInfo line, grouping_exception_types t) {
        GroupingExceptionToken gt = new GroupingExceptionToken(line, t);
        for (GrouperToken g : groupers) {
            g.put(gt);
        }
    }

    public void parse() {
        while (true) {
            GrouperToken top_of_stack = groupers.peek();
            try {
                Token t = lexer.yylex();
                if (t instanceof EOF_Token) {
                    if (groupers.size() != 1) {
                        TossException(((EOF_Token) t)
                                .getClosingException(top_of_stack));
                    } else {
                        top_of_stack.put(t);
                    }
                    return;
                } else if (t instanceof ClosingToken) {
                    GroupingException g = ((ClosingToken) t)
                            .getClosingException(top_of_stack);
                    if (g == null) {
                        top_of_stack.put(new EOF_Token(t.lineInfo));
                        groupers.pop();
                        continue;
                    } else {
                        TossException(g);
                        return;
                    }
                }
                if (t instanceof WarningToken) {
                    // TODO handle warnings...
                    continue;
                }

                // Everything else passes through normally.
                top_of_stack.put(t);
                if (t instanceof GrouperToken) {
                    groupers.push((GrouperToken) t);
                }
            } catch (IOException e) {
                EnumeratedGroupingException g = new EnumeratedGroupingException(
                        top_of_stack.lineInfo,
                        grouping_exception_types.IO_EXCEPTION);
                g.caused = e;
                TossException(g);
                return;
            }
        }
    }

    @Override
    public void run() {
        parse();
    }
}
