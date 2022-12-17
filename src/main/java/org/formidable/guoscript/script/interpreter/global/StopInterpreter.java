package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.script.NextAction;

public class StopInterpreter extends Interpreter {
    public StopInterpreter(){
        setName("stop");
        setHead("en", "stop");
        setHead("zh", "停止运行");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        return new ExecuteResult(scriptLine, true, NextAction.STOP);
    }
}
