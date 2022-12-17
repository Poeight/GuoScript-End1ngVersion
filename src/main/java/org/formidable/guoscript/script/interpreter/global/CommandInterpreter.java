package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class CommandInterpreter extends Interpreter {
    public CommandInterpreter(){
        setName("command");
        setHead("en", "command");
        setHead("zh", "玩家指令");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String command = scriptLine.substring(head.length() + 1, scriptLine.lastIndexOf(")->"));
        String target = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        if (target.equalsIgnoreCase("*")){
            for (Player player : Bukkit.getOnlinePlayers()){
                Bukkit.dispatchCommand(player, command);
            }
        }else {
            Player player = Bukkit.getPlayer(target);
            if (player != null && player.isOnline()){
                Bukkit.dispatchCommand(player, command);
            }
        }
        return new ExecuteResult();
    }
}
