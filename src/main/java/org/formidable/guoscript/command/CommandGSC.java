package org.formidable.guoscript.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.formidable.guoscript.GuoScript;
import org.formidable.guoscript.manager.script.CommandScriptManager;

public class CommandGSC implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()){
            return true;
        }
        CommandScriptManager commandScriptManager = GuoScript.getCommandScriptManager();
        if (args.length >= 1){
            if (args[0].equalsIgnoreCase("list")){
                int page = 1;
                if (args.length >= 2){
                    try {
                        page = Integer.valueOf(args[1]);
                    }catch (Exception e){
                        sender.sendMessage("§7页码必须是数字.");
                        return true;
                    }
                }
                commandScriptManager.showScriptNames(sender, page);
                return true;
            }
            if (args[0].equalsIgnoreCase("run")){
                if (args.length >= 2){
                    String scriptName = args[1];
                    if (commandScriptManager.getScript(scriptName) == null){
                        sender.sendMessage("§7指定脚本" + args[1] + "未被载入.");
                        return true;
                    }
                    if (args.length >= 3){
                        String playerName = args[2];
                        Player player = Bukkit.getPlayer(playerName);
                        if (player == null){
                            sender.sendMessage("§7指定玩家不在线.");
                            return true;
                        }
                        commandScriptManager.runScript(scriptName, playerName);
                        sender.sendMessage("§7开始为玩家" + playerName + "执行脚本: " + scriptName);
                        return true;
                    }
                    commandScriptManager.runScript(scriptName);
                    sender.sendMessage("§7开始执行脚本: " + scriptName);
                    return true;
                }
            }
        }
        sender.sendMessage("§6/gsc list [页码] §7列出载入的指令型脚本的名字");
        sender.sendMessage("§6/gsc run <脚本名> §7运行指定脚本");
        sender.sendMessage("§6/gsc run <脚本名> <玩家名> §7为玩家运行指定脚本");
        return true;
    }

}
