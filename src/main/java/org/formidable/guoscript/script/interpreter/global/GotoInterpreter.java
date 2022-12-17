package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class GotoInterpreter extends Interpreter {
    public GotoInterpreter(){
        setName("goto");
        setHead("en", "goto");
        setHead("zh", "跳转运行");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        Integer index = Integer.valueOf(scriptLine.substring(head.length() + 1, scriptLine.indexOf(")"))) - 1;
        getHandler().getRunningScript(runningScriptID).setScriptIndex(index);
        return new ExecuteResult();
    }
}
