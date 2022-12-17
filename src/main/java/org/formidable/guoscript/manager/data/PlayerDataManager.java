package org.formidable.guoscript.manager.data;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.formidable.guoscript.manager.data.saver.DataSaver;
import org.formidable.guoscript.manager.data.saver.FileDataSaver;
import org.formidable.guoscript.manager.data.saver.MysqlDataSaver;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager implements Listener {

    private JavaPlugin plugin;

    private DataSaver dataSaver;

    // 小写的玩家名 -> 玩家数据对象
    private ConcurrentHashMap<String, PlayerData> playerDataMap = new ConcurrentHashMap<>();

    public PlayerDataManager(JavaPlugin plugin){
        this.plugin = plugin;
        if (plugin.getConfig().getBoolean("mysql.enable")){
            try {
                dataSaver = new MysqlDataSaver(plugin);
            } catch (SQLException e) {
                e.printStackTrace();
                dataSaver = new FileDataSaver(plugin);
            }
        }else {
            dataSaver = new FileDataSaver(plugin);
        }
        // 异步每3分钟执行一次
        new BukkitRunnable() {
            @Override
            public void run() {
                // 清除不在线玩家的数据
                for (String playerName : playerDataMap.keySet()){
                    if (Bukkit.getPlayer(playerName) == null){
                        playerDataMap.remove(playerName);
                    }
                }
                // 储存所有数据
                saveAllPlayerData();
            }
        }.runTaskTimerAsynchronously(plugin, 20 * 60 * 3, 20 * 60 * 3);
    }

    public PlayerData getPlayerData(String playerName){
        playerName = playerName.toLowerCase();
        PlayerData playerData = playerDataMap.get(playerName);
        if (playerData == null){
            loadPlayerData(playerName);
        }
        return playerDataMap.get(playerName);
    }

    private ConcurrentHashMap<String, Boolean> loadingMap = new ConcurrentHashMap<>();

    public synchronized void loadPlayerData(String playerName){
        playerName = playerName.toLowerCase();
        loadingMap.put(playerName, true);
        PlayerData playerData = dataSaver.load(playerName);
        playerDataMap.put(playerName, playerData);
        loadingMap.remove(playerName);
    }

    public synchronized void savePlayerData(String playerName){
        playerName = playerName.toLowerCase();
        if (loadingMap.containsKey(playerName)){
            return;
        }
        PlayerData playerData = playerDataMap.get(playerName);
        if (playerData == null){
            return;
        }
        dataSaver.save(playerName, playerData);
    }

    public synchronized void saveAllPlayerData(){
        dataSaver.saveAll(playerDataMap);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        String playerName = e.getPlayer().getName().toLowerCase();
        new BukkitRunnable(){
            @Override
            public void run() {
                loadPlayerData(playerName);
            }
        }.runTaskLaterAsynchronously(plugin, 10);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        String playerName = e.getPlayer().getName().toLowerCase();
        new BukkitRunnable(){
            @Override
            public void run() {
                savePlayerData(playerName);
            }
        }.runTaskAsynchronously(plugin);
    }
}
