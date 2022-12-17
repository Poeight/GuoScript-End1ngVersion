package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class HasPermInterpreter extends Interpreter {

    public HasPermInterpreter(){
        setName("hasPerm");
        setHead("en", "hasPerm");
        setHead("zh", "拥有权限条件");
    }

    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String[] pp = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->")).split("[:]");
        String playerName = pp[0];
        String permission = pp[1];
        String conditionName = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && player.isOnline()){
            if (player.hasPermission(permission)){
                getHandler().getRunningScript(runningScriptID).setCondition(conditionName, true);
            }
        }
        return new ExecuteResult();
    }
}
