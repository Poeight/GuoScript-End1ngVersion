package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.script.RunningScript;
import org.formidable.guoscript.util.RandomUtil;

public class ChanceInterpreter extends Interpreter {

    public ChanceInterpreter(){
        setName("chance");
        setHead("en", "chance");
        setHead("zh", "概率随机条件");
    }

    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        double chance = Double.valueOf(scriptLine.substring(head.length() + 1, scriptLine.lastIndexOf(")->")));
        String conditionName = scriptLine.substring(scriptLine.lastIndexOf(")->") + 3);
        RunningScript runningScript = getHandler().getRunningScript(runningScriptID);
        if (chance / 100 >= RandomUtil.RANDOM.nextDouble()){
            runningScript.setCondition(conditionName, true);
        }else {
            runningScript.setCondition(conditionName, false);
        }
        return new ExecuteResult();
    }
}
