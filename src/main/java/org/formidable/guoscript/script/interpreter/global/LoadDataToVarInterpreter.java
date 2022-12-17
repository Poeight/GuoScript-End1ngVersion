package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.GuoScript;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class LoadDataToVarInterpreter extends Interpreter {
    public LoadDataToVarInterpreter(){
        setName("loadDataToVar");
        setHead("en", "loadDataToVar");
        setHead("zh", "读取数据至变量");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String[] pd = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->")).split(":");
        String playerName = pd[0];
        String dataName = pd[1];
        String varName = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        String value = GuoScript.getPlayerDataManager().getPlayerData(playerName).getData(dataName);
        getHandler().getRunningScript(runningScriptID).setVar(varName, value);
        return new ExecuteResult();
    }
}
