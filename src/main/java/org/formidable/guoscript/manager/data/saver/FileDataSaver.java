package org.formidable.guoscript.manager.data.saver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.formidable.guoscript.manager.data.PlayerData;
import org.formidable.guoscript.wrapper.ConfigWrapper;

import java.util.Map;

public class FileDataSaver implements DataSaver {
    JavaPlugin plugin;
    public FileDataSaver(JavaPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public synchronized PlayerData load(String playerName) {
        playerName = playerName.toLowerCase();
        PlayerData playerData = new PlayerData(playerName);
        ConfigWrapper configWrapper = new ConfigWrapper(plugin, "data\\" + playerName + ".yml");
        FileConfiguration config = configWrapper.getConfig();
        for (String dataName : config.getKeys(false)){
            playerData.setData(dataName, config.getString(dataName));
        }
        return playerData;
    }

    @Override
    public synchronized void save(String playerName, PlayerData playerData) {
        playerName = playerName.toLowerCase();
        ConfigWrapper configWrapper = new ConfigWrapper(plugin, "data\\" + playerName + ".yml");
        FileConfiguration config = configWrapper.getConfig();
        for (String dataName : playerData.getDataMap().keySet()){
            config.set(dataName, playerData.getData(dataName));
        }
        configWrapper.save();
    }

    @Override
    public void saveAll(Map<String, PlayerData> map) {
        for (String playerName : map.keySet()){
            save(playerName, map.get(playerName));
        }
    }
}
