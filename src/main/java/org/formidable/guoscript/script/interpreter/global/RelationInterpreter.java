package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.script.RunningScript;
import org.formidable.guoscript.util.ScriptEngineUtil;

public class RelationInterpreter extends Interpreter {

    public RelationInterpreter(){
        setName("relation");
        setHead("en", "relation");
        setHead("zh", "关系表达式条件");
    }

    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String expression = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->"));
        String conditionName = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        Boolean b = (Boolean) ScriptEngineUtil.ENGINE.eval(expression);
        RunningScript runningScript = getHandler().getRunningScript(runningScriptID);
        runningScript.setCondition(conditionName, b);
        return new ExecuteResult();
    }
}
