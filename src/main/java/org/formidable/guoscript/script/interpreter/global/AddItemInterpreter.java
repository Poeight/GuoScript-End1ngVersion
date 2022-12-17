package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.formidable.guoscript.GuoScript;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.script.NextAction;
import org.formidable.guoscript.wrapper.ItemWrapper;

public class AddItemInterpreter extends Interpreter {

    public AddItemInterpreter(){
        setName("addItem");
        setHead("en", "addItem");
        setHead("zh", "给予物品");
    }

    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String[] ia = scriptLine.substring(head.length() + 1, scriptLine.indexOf(")->")).split(":");
        String itemName = ia[0];
        Integer amount = Integer.valueOf(ia[1]);
        String playerName = scriptLine.substring(scriptLine.indexOf(")->") + 3);
        Player player = Bukkit.getPlayer(playerName);
        if (player != null && player.isOnline()){
            ItemWrapper itemWrapper = GuoScript.getItemManager().getItem(itemName);
            if (itemWrapper == null){
                getHandler().log("物品管理器中不存在物品: " + itemName + " ，脚本已停止运行");
                return new ExecuteResult(scriptLine, false, NextAction.STOP);
            }
            if (amount > 0){
                for (int i = 0; i < amount; ++i){
                    player.getInventory().addItem(itemWrapper.getItemStack(1));
                }
            }else {
                amount = -amount;
                ItemStack itemStack = itemWrapper.getItemStack(1);
                PlayerInventory playerInventory = player.getInventory();
                for (int o = 0; o < playerInventory.getSize(); ++o){
                    ItemStack i = playerInventory.getItem(o);
                    if (i == null){
                        continue;
                    }
                    if (itemStack.isSimilar(i)){
                        int itemAmount = i.getAmount();
                        if (itemAmount > amount){
                            i.setAmount(itemAmount - amount);
                            playerInventory.setItem(o, i);
                            amount = 0;
                        }else {
                            amount = amount - itemAmount;
                            playerInventory.setItem(o, null);
                        }
                        if (amount == 0){
                            break;
                        }
                    }
                }
            }
        }
        return new ExecuteResult();
    }
}
