package org.formidable.guoscript.script.interpreter.global;

import org.formidable.guoscript.GuoScript;
import org.formidable.guoscript.manager.data.PlayerData;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class SetDataInterpreter extends Interpreter {
    public SetDataInterpreter(){
        setName("setData");
        setHead("en", "setData");
        setHead("zh", "设置数据");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String[] dv = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->")).split(":");
        String dataName = dv[0];
        String value = dv[1];
        String playerName = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        PlayerData playerData = GuoScript.getPlayerDataManager().getPlayerData(playerName);
        playerData.setData(dataName, value);
        return new ExecuteResult();
    }
}
