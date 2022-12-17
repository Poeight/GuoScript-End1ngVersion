package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.formidable.guoscript.GuoScript;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.script.NextAction;
import org.formidable.guoscript.script.RunningScript;
import org.formidable.guoscript.wrapper.ItemWrapper;

public class HasItemInterpreter extends Interpreter {

    public HasItemInterpreter() {
        setName("hasItem");
        setHead("en", "hasItem");
        setHead("zh", "拥有物品条件");
    }

    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String[] pia = scriptLine.substring(head.length() +1, scriptLine.indexOf(")->")).split(":");
        String playerName = pia[0];
        String itemName = pia[1];
        Integer amount = Integer.valueOf(pia[2]);
        String[] cl = scriptLine.split("[)][-][>]")[1].split("[-][>]");
        String conditionName = cl[0];
        String less = cl[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && player.isOnline()){
            ItemWrapper itemWrapper = GuoScript.getItemManager().getItem(itemName);
            if (itemWrapper == null){
                getHandler().log("物品管理器中不存在物品: " + itemName + " ，脚本已停止运行");
                return new ExecuteResult(scriptLine, false, NextAction.STOP);
            }
            ItemStack itemStack = itemWrapper.getItemStack();
            for (ItemStack i : player.getInventory()){
                if (itemStack.isSimilar(i)){
                    amount = amount - i.getAmount();
                }
                if (amount <= 0){
                    break;
                }
            }
            RunningScript runningScript = getHandler().getRunningScript(runningScriptID);
            if (amount <= 0){
                runningScript.setCondition(conditionName, true);
            }else {
                runningScript.setCondition(conditionName, false);
                runningScript.setVar(less, amount.toString());
            }
        }
        return new ExecuteResult();
    }
}
