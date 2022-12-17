package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class PauseInterpreter extends Interpreter {
    public PauseInterpreter(){
        setName("pause");
        setHead("en", "pause");
        setHead("zh", "暂停运行");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        Integer tick = Integer.valueOf(scriptLine.substring(head.length() + 1, scriptLine.indexOf(")")));
        getHandler().getRunningScript(runningScriptID).addPause(tick);
        return new ExecuteResult();
    }
}
