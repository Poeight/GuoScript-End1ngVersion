package org.formidable.guoscript.script.interpreter.global;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.script.NextAction;

public class LoadPAPIToVarInterpreter extends Interpreter {
    public LoadPAPIToVarInterpreter(){
        setName("loadPAPIToVar");
        setHead("en", "loadPAPIToVar");
        setHead("zh", "读取占位符至变量");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        if (!getHandler().isEnablePAPI()){
            getHandler().log("未启用插件PlaceholderAPI，脚本已停止运行");
            return new ExecuteResult(scriptLine, false, NextAction.STOP);
        }
        String[] pp = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->")).split(":");
        String varName = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        String playerName = pp[0];
        String placeholder = pp[1];
        Player player = Bukkit.getPlayer(playerName);
        placeholder = PlaceholderAPI.setPlaceholders(player, placeholder);
        getHandler().getRunningScript(runningScriptID).setVar(varName, placeholder);
        return new ExecuteResult();
    }
}
