package org.formidable.guoscript.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.formidable.guoscript.GuoScript;

public class CommandGS implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()){
            return true;
        }
        if (args.length >= 1){
            if (args[0].equalsIgnoreCase("reload")){
                sender.sendMessage("§7插件重载成功.");
                GuoScript.getInstance().reload();
                return true;
            }
        }
        sender.sendMessage("§6/gs reload §7重载插件");
        sender.sendMessage("§6/gsi §7物品管理器相关指令帮助");
        sender.sendMessage("§6/gsd §7数据管理器相关指令帮助");
        sender.sendMessage("§6/gsc §7指令型脚本相关指令帮助");
        return true;
    }

}
