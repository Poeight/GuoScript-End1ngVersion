package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.hook.VaultHook;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.util.FormatUtil;

public class HasMoneyInterpreter extends Interpreter {
    public HasMoneyInterpreter(){
        setName("hasMoney");
        setHead("en", "hasMoney");
        setHead("zh", "拥有金钱");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String[] pa = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->")).split(":");
        String playerName = pa[0];
        Double amount = Double.valueOf(pa[1]);
        String[] cl = scriptLine.substring(scriptLine.indexOf(")->") + 3).split("[-][>]");
        String conditionName = cl[0];
        String less = cl[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && player.isOnline()){
            if (VaultHook.getMoney(player) >= amount){
                getHandler().getRunningScript(runningScriptID).setCondition(conditionName, true);
            }else {
                getHandler().getRunningScript(runningScriptID).setVar(less, FormatUtil.round(amount - VaultHook.getMoney(player)));
            }
        }
        return new ExecuteResult();
    }
}
