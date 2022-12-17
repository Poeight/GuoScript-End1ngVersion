package org.formidable.guoscript.command;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.formidable.guoscript.GuoScript;
import org.formidable.guoscript.manager.data.PlayerData;
import org.formidable.guoscript.method.GetRandomNumber;
import org.formidable.guoscript.util.FormatUtil;

import java.text.DecimalFormat;

public class CommandGSD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()){
            return true;
        }
        if (args.length >= 2){
            if (args[0].equalsIgnoreCase("list")) {
                String playerName = args[1];
                Player player = Bukkit.getPlayer(playerName);
                if (player == null){
                    sender.sendMessage("§7指定玩家不在线.");
                    return true;
                }
                PlayerData playerData = GuoScript.getPlayerDataManager().getPlayerData(playerName);
                int page = 1;
                if (args.length >= 3) {
                    try {
                        page = Integer.valueOf(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage("§7页码必须是数字.");
                        return true;
                    }
                }
                playerData.showData(sender, page);
                return true;
            }
        }
        if (args.length == 3){
            if (args[0].equalsIgnoreCase("get")){
                String playerName = args[1];
                Player player = Bukkit.getPlayer(playerName);
                if (player == null){
                    sender.sendMessage("§7指定玩家不在线.");
                    return true;
                }
                PlayerData playerData = GuoScript.getPlayerDataManager().getPlayerData(playerName);
                if(sender.isOp()){
                    sender.sendMessage("§7玩家" + playerName + "的名为" + args[2] + "的数据的值为: " + playerData.getData(args[2]));
                }
                return true;
            }
        }
        if (args.length >= 4){
            if (args[0].equalsIgnoreCase("set")){
                String playerName = args[1];
                Player player = Bukkit.getPlayer(playerName);
                if (player == null){
                    sender.sendMessage("§7指定玩家不在线.");
                    return true;
                }
                PlayerData playerData = GuoScript.getPlayerDataManager().getPlayerData(playerName);
                playerData.setData(args[2], args[3]);
                sender.sendMessage("§7设置成功.");
                return true;
            }
            if (args[0].equalsIgnoreCase("add")){
                String playerName = args[1];
                Player player = Bukkit.getPlayer(playerName);
                if (player == null){
                    sender.sendMessage("§7指定玩家不在线.");
                    return true;
                }
                PlayerData playerData = GuoScript.getPlayerDataManager().getPlayerData(playerName);
                String dataName = args[2];
                Double add;
                try {
                    add = Double.valueOf(args[3]);
                } catch (Exception e) {
                    sender.sendMessage("§7增加量必须是数字.");
                    return true;
                }
                Double value;
                try {
                    value = Double.valueOf(playerData.getData(dataName));
                } catch (Exception e) {
                    sender.sendMessage("§7数据类型必须是数字.");
                    return true;
                }
                value = value + add;
                playerData.setData(dataName, FormatUtil.format(value.toString()));
                if(sender.isOp()){
                sender.sendMessage("§7执行增加后，玩家" + playerName + "的名为" + dataName + "的数据的值现在是: " + add);
                sender.sendMessage("§7执行增加后，玩家" + playerName + "的名为" + dataName + "的数据的值现在是: " + playerData.getData(dataName));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("take")){
                String playerName = args[1];
                Player player = Bukkit.getPlayer(playerName);
                if(player == null){
                    sender.sendMessage("§7指定玩家不在线.");
                    return true;
                }
                PlayerData playerData = GuoScript.getPlayerDataManager().getPlayerData(playerName);
                String dataName = args[2];
                Double take;
                try {
                    take = Double.valueOf(args[3]);
                } catch (Exception e) {
                    sender.sendMessage("§7删减量必须是数字.");
                    return true;
                }
                Double value;
                try {
                    value = Double.valueOf(playerData.getData(dataName));
                } catch (Exception e) {
                    sender.sendMessage("§7数据类型必须是数字.");
                    return true;
                }
                value = value - take;
                playerData.setData(dataName, FormatUtil.format(value.toString()));
                if(sender.isOp()){
                    sender.sendMessage("§7执行删减后，玩家" + playerName + "的名为" + dataName + "的数据的值现在是: " + take);
                    sender.sendMessage("§7执行删减后，玩家" + playerName + "的名为" + dataName + "的数据的值现在是: " + playerData.getData(dataName));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("random")){
                String playerName = args[1];
                Player player = Bukkit.getPlayer(playerName);
                if(player == null){
                    sender.sendMessage("§7指定玩家不在线.");
                    return true;
                }
                PlayerData playerData = GuoScript.getPlayerDataManager().getPlayerData(playerName);
                String dataName = args[2];
                double result,endResult;
                try {
                    double randomNumber = Math.random();
                    double multiple = Double.parseDouble(args[3]);
                    result = randomNumber * multiple;
                    String str = String.format("%.2f",result);
                    endResult = Double.parseDouble(str);
                } catch (Exception e) {
                    sender.sendMessage("§7倍数必须为数字.");
                    return true;
                }
                Double value;
                try {
                    value = Double.valueOf(playerData.getData(dataName));
                } catch (Exception e) {
                    sender.sendMessage("§7数据类型必须是数字.");
                    return true;
                }
                value = value + endResult;
                playerData.setData(dataName, FormatUtil.format(value.toString()));
                if(sender.isOp()){
                    sender.sendMessage("§7执行随机倍率取值后，玩家" + playerName + "的名为" + dataName + "的数据的值变化为: " + endResult);
                    sender.sendMessage("§7执行随机倍率取值后，玩家" + playerName + "的名为" + dataName + "的数据的值现在是: " + playerData.getData(dataName));
                }
                return true;
            }
        }
        sender.sendMessage("§6/gsd list <玩家名> [页码] §7列出指定玩家的数据");
        sender.sendMessage("§6/gsd get <玩家名> <数据名> §7查询指定玩家的指定数据");
        sender.sendMessage("§6/gsd set <玩家名> <数据名> <值> §7设置指定玩家的指定数据");
        sender.sendMessage("§6/gsd add <玩家名> <数据名> <增加量> §7增加玩家的指定数字型数据");
        sender.sendMessage("§6/gsd take <玩家名> <数据名> <删减量> §7删减玩家的指定数字型数据");
        sender.sendMessage("§6/gsd random <玩家名> <数据名> <增加比例> §7随机增加玩家的指定数据(为1则取值为0-1任意数字)");
        return true;
    }
}
