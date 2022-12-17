package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.script.NextAction;
import org.formidable.guoscript.script.RunningScript;

public class IfInterpreter extends Interpreter {

    public IfInterpreter() {
        setName("if");
        setHead("en", "if");
        setHead("zh", "判断");
    }

    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) {
        String conditionName = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->"));
        boolean b = true;
        while (conditionName.startsWith("!")) {
            b = !b;
            conditionName = conditionName.substring(1);
        }
        RunningScript runningScript = getHandler().getRunningScript(runningScriptID);
        if (!b){
            b = !runningScript.getCondition(conditionName);
        }else {
            b = runningScript.getCondition(conditionName);
        }
        if (b) {
            scriptLine = scriptLine.substring(scriptLine.indexOf(")->") + 3);
            return new ExecuteResult(scriptLine, true, NextAction.REPEAT);
        } else {
            return new ExecuteResult();
        }
    }

}
