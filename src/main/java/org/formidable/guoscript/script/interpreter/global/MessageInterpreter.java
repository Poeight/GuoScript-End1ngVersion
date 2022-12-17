package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.util.FormatUtil;

public class MessageInterpreter extends Interpreter {
    public MessageInterpreter(){
        setName("message");
        setHead("en", "message");
        setHead("zh", "发送信息");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String message = scriptLine.substring(head.length() + 1, scriptLine.lastIndexOf(")->")).replace("&", "§");
        message = FormatUtil.roundAll(message);
        String target = scriptLine.substring(scriptLine.lastIndexOf(")->") + 3);
        if (target.equalsIgnoreCase("*")){
            Bukkit.broadcastMessage(message);
        }else {
            Player player = Bukkit.getPlayer(target);
            if (player != null && player.isOnline()){
                player.sendMessage(message);
            }
        }
        return new ExecuteResult();
    }
}
