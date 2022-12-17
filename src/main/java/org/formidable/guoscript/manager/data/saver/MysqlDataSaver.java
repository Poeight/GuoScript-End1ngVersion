package org.formidable.guoscript.manager.data.saver;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.formidable.guoscript.manager.data.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MysqlDataSaver implements DataSaver {
    JavaPlugin plugin;
    String table;

    Connection connection;
    public MysqlDataSaver(JavaPlugin plugin) throws SQLException {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        MysqlDataSource mds = new MysqlDataSource();
        mds.setURL(config.getString("mysql.url"));
        mds.setUser(config.getString("mysql.username"));
        mds.setPassword(config.getString("mysql.password"));
        this.table = config.getString("mysql.table");
        mds.setCacheCallableStmts(true);
        mds.setConnectTimeout(1000);
        mds.setLoginTimeout(2000);
        mds.setUseUnicode(true);
        mds.setEncoding("UTF-8");
        mds.setZeroDateTimeBehavior("convertToNull");
        mds.setMaxReconnects(5);
        mds.setAutoReconnect(true);
        connection = mds.getConnection();

        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
                "`playername` varchar(16), " +
                "`dataname` varchar(16), " +
                "`value` varchar(128), " +
                "primary key (`playername`, `dataname`)" +
                ");");
        ps.execute();
        ps.close();
    }
    @Override
    public synchronized PlayerData load(String playerName) {
        playerName = playerName.toLowerCase();
        PlayerData playerData = new PlayerData(playerName);
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT `dataname`,`value` FROM `" + table + "` WHERE `playername`=?;");
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                playerData.setData(rs.getString("dataname"), rs.getString("value"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerData;
    }

    @Override
    public synchronized void save(String playerName, PlayerData playerData) {
        playerName = playerName.toLowerCase();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `" + table + "` VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `value`=?;");
            for (String dataName : playerData.getDataMap().keySet()){
                ps.setString(1, playerName);
                ps.setString(2, dataName);
                ps.setString(3, playerData.getData(dataName));
                ps.setString(4, playerData.getData(dataName));
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll(Map<String, PlayerData> map) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `" + table + "` VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `value`=?;");
            for (String playerName : map.keySet()){
                PlayerData playerData = map.get(playerName);
                playerName = playerName.toLowerCase();
                for (String dataName : playerData.getDataMap().keySet()){
                    ps.setString(1, playerName);
                    ps.setString(2, dataName);
                    ps.setString(3, playerData.getData(dataName));
                    ps.setString(4, playerData.getData(dataName));
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            ps.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
