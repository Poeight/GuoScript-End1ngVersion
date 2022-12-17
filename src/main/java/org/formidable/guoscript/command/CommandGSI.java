package org.formidable.guoscript.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.formidable.guoscript.GuoScript;
import org.formidable.guoscript.wrapper.ItemWrapper;

public class CommandGSI implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                int page = 1;
                if (args.length >= 2) {
                    try {
                        page = Integer.valueOf(args[1]);
                    } catch (Exception e) {
                        sender.sendMessage("§7页码必须是数字.");
                        return true;
                    }
                }
                GuoScript.getItemManager().showItemName(sender, page);
                return true;
            }
        }
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("get")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§7仅限玩家使用.");
                }
                Player player = (Player) sender;
                String path = args[1];
                ItemWrapper itemWrapper = GuoScript.getItemManager().getItem(path);
                if (itemWrapper == null) {
                    sender.sendMessage("§7指定道具不存在.");
                    return true;
                }
                int amount = 1;
                if (args.length >= 3) {
                    try {
                        amount = Integer.valueOf(args[2]);
                    } catch (Exception e) {
                        player.sendMessage("§7数量必须为数字.");
                        return true;
                    }
                }
                giveItem(player, itemWrapper, amount);
                sender.sendMessage("§7获取成功.");
                return true;
            }
            if (args[0].equalsIgnoreCase("save")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    String path = args[1];
                    ItemStack itemStack = player.getItemInHand();
                    if (itemStack == null || itemStack.getType() == Material.AIR) {
                        player.sendMessage("§7必须手持物品才能储存.");
                        return true;
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            GuoScript.getItemManager().saveItem(path, itemStack);
                            sender.sendMessage("§7储存成功.");
                        }
                    }.runTaskAsynchronously(GuoScript.getInstance());
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("delete")) {
                String path = args[1];
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (GuoScript.getItemManager().deleteItem(path)) {
                            sender.sendMessage("§7删除成功.");
                        } else {
                            sender.sendMessage("§7指定物品不存在");
                        }
                    }
                }.runTaskAsynchronously(GuoScript.getInstance());
                return true;
            }
        }
        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("give")) {
                String playerName = args[1];
                String path = args[2];
                Integer amount = 1;
                if (args.length >= 4) {
                    try {
                        amount = Integer.valueOf(args[3]);
                    } catch (Exception e) {
                        sender.sendMessage("§7数量必须为数字.");
                        return true;
                    }
                }
                Player player = Bukkit.getPlayer(playerName);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage("§7指定玩家不在线.");
                    return true;
                }
                ItemWrapper itemWrapper = GuoScript.getItemManager().getItem(path);
                if (itemWrapper == null) {
                    sender.sendMessage("§7指定道具不存在.");
                    return true;
                }
                giveItem(player, itemWrapper, amount);
                sender.sendMessage("§7给予成功.");
                return true;
            }
        }
        sender.sendMessage("§6/gsi list [页码] §7列出载入的物品名");
        sender.sendMessage("§6/gsi get <物品名> [数量] §7获取储存的物品");
        sender.sendMessage("§6/gsi save <物品名> §7将物品储存供调用");
        sender.sendMessage("§6/gsi delete <物品名> §7将储存的物品删除");
        sender.sendMessage("§6/gsi give <玩家名> <物品名> [数量] §7给予指定玩家物品");
        return true;
    }

    private void giveItem(Player player, ItemWrapper itemWrapper, Integer amount) {
        ItemStack itemStack = itemWrapper.getItemStack(1);
        for (int i = 0; i < amount; ++i) {
            for (ItemStack is : player.getInventory().addItem(itemStack).values()){
                player.getWorld().dropItem(player.getLocation(), is);
            }
        }
    }
}
