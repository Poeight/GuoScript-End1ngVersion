package org.formidable.guoscript.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.formidable.guoscript.GuoScript;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private JavaPlugin plugin;

    @Override
    public String getIdentifier() {
        return "gs";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist(){
        return true;
    }

    public PlaceholderAPIHook(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (params.startsWith("data_")){
            if (player != null){
                String dataName = params.split("_")[1];
                return GuoScript.getPlayerDataManager().getPlayerData(player.getName()).getData(dataName);
            }
        }
        return null;
    }
}
