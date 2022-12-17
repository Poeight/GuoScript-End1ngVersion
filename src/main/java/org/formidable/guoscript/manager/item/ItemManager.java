package org.formidable.guoscript.manager.item;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.formidable.guoscript.wrapper.ConfigWrapper;
import org.formidable.guoscript.wrapper.ItemWrapper;

import java.io.File;
import java.util.*;

public class ItemManager {

    private JavaPlugin plugin;
    // 文件名 -> 物品名
    private Map<String, LinkedHashMap<String, ItemWrapper>> fileMap = new LinkedHashMap<>();

    public ItemManager(JavaPlugin plugin){

        this.plugin = plugin;

        if (plugin.getConfig().getBoolean("settings.createExample")){
            new ConfigWrapper(plugin, "items\\example.yml");
        }

        File main = new File(plugin.getDataFolder().getAbsolutePath() + "\\items");
        File[] files = main.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                if (!file.getName().endsWith(".yml")){
                    continue;
                }
                String fileName = file.getName().replace(".yml", "");
                ConfigWrapper configWrapper = new ConfigWrapper(file);
                LinkedHashMap<String, ItemWrapper> itemMap = new LinkedHashMap<>();
                fileMap.put(fileName, itemMap);
                for (String key : configWrapper.getConfig().getKeys(false)){
                    ItemStack itemStack = configWrapper.getConfig().getItemStack(key);
                    if (itemStack == null){
                        configWrapper.getConfig().set(key, null);
                        continue;
                    }
                    ItemWrapper itemWrapper = new ItemWrapper(key, itemStack);
                    itemMap.put(key, itemWrapper);
                }
                configWrapper.save();
            }
        }
        updatePage();
    }

    public ItemWrapper getItem(String path){
        String[] s = splitPath(path);
        String file = s[0];
        String name = s[1];
        if (fileMap.containsKey(file)){
            LinkedHashMap<String, ItemWrapper> itemMap = fileMap.get(file);
            for (ItemWrapper itemWrapper : itemMap.values()){
                if (itemWrapper.getName().equalsIgnoreCase(name)){
                    return itemWrapper;
                }
            }
        }
        return null;
    }

    public synchronized void saveItem(String path, ItemStack itemStack){
        String[] s = splitPath(path);
        String file = s[0];
        String name = s[1];
        LinkedHashMap<String, ItemWrapper> itemMap = fileMap.get(file);
        if (itemMap == null){
            itemMap = new LinkedHashMap<>();
            fileMap.put(file, itemMap);
        }
        ItemWrapper itemWrapper = new ItemWrapper(name, itemStack);
        itemMap.put(name, itemWrapper);
        ConfigWrapper configWrapper = new ConfigWrapper(plugin, "items\\" + file + ".yml");
        configWrapper.getConfig().set(itemWrapper.getName(), itemWrapper.getItemStack());
        configWrapper.save();
        updatePage();
    }

    public synchronized boolean deleteItem(String path){
        String[] s = splitPath(path);
        String file = s[0];
        String name = s[1];
        if (fileMap.containsKey(file)){
            HashMap<String, ItemWrapper> itemMap = fileMap.get(file);
            for (ItemWrapper itemWrapper : itemMap.values()){
                if (itemWrapper.getName().equalsIgnoreCase(name)){
                    ConfigWrapper configWrapper = new ConfigWrapper(plugin, "items\\" + file + ".yml");
                    configWrapper.getConfig().set(itemWrapper.getName(), null);
                    configWrapper.save();
                    itemMap.remove(itemWrapper.getName());
                    updatePage();
                    return true;
                }
            }
        }
        return false;
    }

    private String[] splitPath(String path){
        String file = "default";
        String name;
        if (path.contains(".")){
            String[] str = path.split("\\.");
            file = str[0];
            name = str[1];
        }else {
            name = path;
        }
        return new String[] {file, name};
    }

    private int MAX_PAGE;

    private void updatePage(){
        MAX_PAGE = 0;
        for (Map map : fileMap.values()){
            MAX_PAGE += map.values().size();
        }
        MAX_PAGE = MAX_PAGE / 8;
    }

    public void showItemName(CommandSender sender, int page){
        page = page - 1;
        if (page > MAX_PAGE){
            page = MAX_PAGE;
        }
        int index = page * 8;
        int amount = 8;
        List<String> list = new ArrayList<>();
        boolean end = false;
        for (String file : fileMap.keySet()){
            Map<String, ItemWrapper> map = fileMap.get(file);
            for (String name : map.keySet()){
                if (index > 0){
                    index--;
                }else {
                    if (file.equalsIgnoreCase("default")){
                        list.add(name);
                    }else {
                        list.add(file + "." +  name);
                    }
                    amount--;
                    if (amount == 0){
                        end = true;
                        break;
                    }
                }
            }
            if (end){
                break;
            }
        }
        page = page + 1;
        int max = MAX_PAGE + 1;
        sender.sendMessage("§7§l----====以下是储存的物品名§e(" + page + "/" + max + ")§7§l====----");
        for (String key : list){
            sender.sendMessage("§7- " + key);
        }
        sender.sendMessage("§7§l----====以上是储存的物品名§e(" + page + "/" + max + ")§7§l====----");
        for (int i = 0; i < 8 - list.size(); ++i){
            sender.sendMessage("");
        }
    }
}
