package org.formidable.guoscript.manager.data;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {
    @Getter
    String playerName;

    public PlayerData(String playerName){
        this.playerName = playerName;
    }

    @Getter
    private Map<String, String> dataMap = new LinkedHashMap<>();

    public String getData(String dataName){
        return dataMap.getOrDefault(dataName, "0");
    }

    public void setData(String dataName, String value){
        dataMap.put(dataName, value);
    }

    public void showData(CommandSender sender, int page){
        int maxPage = dataMap.size() / 8;
        page = page - 1;
        if (page > maxPage){
            page = maxPage;
        }
        int index = page * 8;
        int amount = 8;
        List<String> list = new ArrayList<>();
        for (String dataName : dataMap.keySet()){
            if (index > 0){
                index--;
            }else {
                list.add(dataName + ": " + dataMap.get(dataName));
                amount = amount - 1;
                if (amount == 0){
                    break;
                }
            }
        }
        page = page + 1;
        maxPage = maxPage + 1;
        sender.sendMessage("§7§l----====以下是储存的数据§e(" + page + "/" + maxPage + ")§7§l====----");
        for (String key : list){
            sender.sendMessage("§7- " + key);
        }
        sender.sendMessage("§7§l----====以上是储存的数据§e(" + page + "/" + maxPage + ")§7§l====----");
        for (int i = 0; i < 8 - list.size(); ++i){
            sender.sendMessage("");
        }
    }
}
