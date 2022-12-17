package org.formidable.guoscript.manager.data.saver;

import org.formidable.guoscript.manager.data.PlayerData;

import java.util.Map;

public interface DataSaver {
    PlayerData load(String playerName);
    void save(String playerName, PlayerData playerData);
    void saveAll(Map<String, PlayerData> map);
}