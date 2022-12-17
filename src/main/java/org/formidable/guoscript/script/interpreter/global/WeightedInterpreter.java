package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.wrapper.WeightedRandomWrapper;

public class WeightedInterpreter extends Interpreter {
    public WeightedInterpreter(){
        setName("weighted");
        setHead("en", "weighted");
        setHead("zh", "加权随机条件");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        WeightedRandomWrapper<String> weightedRandomWrapper = new WeightedRandomWrapper<>();
        String[] str = scriptLine.split("[<][-]");
        for (int i = 1; i < str.length; ++i) {
            String[] str1 = str[i].split(":");
            String conditionName = str1[0];
            String weight = str1[1];
            weightedRandomWrapper.add(conditionName, Integer.valueOf(weight));
        }
        getHandler().getRunningScript(runningScriptID).setCondition(weightedRandomWrapper.nextResult(), true);
        return new ExecuteResult();
    }
}
