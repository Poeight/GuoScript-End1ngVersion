package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.hook.VaultHook;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class AddMoneyInterpreter extends Interpreter {
    public AddMoneyInterpreter(){
        setName("addMoney");
        setHead("en", "addMoney");
        setHead("zh", "给予金钱");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        Double amount = Double.valueOf(scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->")));
        String playerName = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && player.isOnline()){
            VaultHook.addMoney(player, amount);
        }
        return new ExecuteResult();
    }
}
