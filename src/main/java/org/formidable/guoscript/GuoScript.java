package org.formidable.guoscript;

import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.formidable.guoscript.command.CommandGS;
import org.formidable.guoscript.command.CommandGSC;
import org.formidable.guoscript.command.CommandGSD;
import org.formidable.guoscript.command.CommandGSI;
import org.formidable.guoscript.hook.PlaceholderAPIHook;
import org.formidable.guoscript.manager.data.PlayerDataManager;
import org.formidable.guoscript.manager.item.ItemManager;
import org.formidable.guoscript.manager.script.CommandScriptManager;

public final class GuoScript extends JavaPlugin {

    @Getter
    private static GuoScript instance;
    @Getter
    private static ItemManager itemManager;
    @Getter
    private static CommandScriptManager commandScriptManager;
    @Getter
    private static PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        // 统计
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        itemManager = new ItemManager(this);
        playerDataManager = new PlayerDataManager(this);
        getServer().getPluginManager().registerEvents(playerDataManager, this);

        commandScriptManager = new CommandScriptManager(this);

        getCommand("gs").setExecutor(new CommandGS());
        getCommand("gsi").setExecutor(new CommandGSI());
        getCommand("gsd").setExecutor(new CommandGSD());
        getCommand("gsc").setExecutor(new CommandGSC());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(this).register();
        }

        for (Player player : Bukkit.getOnlinePlayers()){
            playerDataManager.loadPlayerData(player.getName());
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()){
            playerDataManager.savePlayerData(player.getName());
        }
    }

    public void reload(){
        HandlerList.unregisterAll(this);

        saveDefaultConfig();
        reloadConfig();

        itemManager = new ItemManager(this);

        commandScriptManager.reload();
    }
}
