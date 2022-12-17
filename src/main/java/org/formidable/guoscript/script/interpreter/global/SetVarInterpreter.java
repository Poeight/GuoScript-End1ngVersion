package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.util.ScriptEngineUtil;

import javax.script.ScriptException;

public class SetVarInterpreter extends Interpreter {
    public SetVarInterpreter(){
        setName("setVar");
        setHead("en", "setVar");
        setHead("zh", "设置变量");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String value = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->"));
        String varName = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        try{
            value = ScriptEngineUtil.ENGINE.eval(value).toString();
        }catch (ScriptException e){

        }
        getHandler().getRunningScript(runningScriptID).setVar(varName, value);
        return new ExecuteResult();
    }
}
