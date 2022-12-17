package org.formidable.guoscript.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ItemWrapper implements ConfigurationSerializable {
    @Getter
    private String name;
    @NonNull
    private ItemStack itemStack;

    public ItemStack getItemStack(){
        if (itemStack == null){
            return null;
        }
        return itemStack.clone();
    }

    public ItemStack getItemStack(int amount){
        if (itemStack == null){
            return null;
        }
        ItemStack itemStack = this.itemStack.clone();
        itemStack.setAmount(amount);
        return itemStack;
    }

    public ItemStack getItemStack(int amount, String...args){
        if (itemStack == null){
            return null;
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length / 2; i = i + 2){
            map.put(args[i], args[i+1]);
        }
        ItemStack itemStack = this.itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName()){
            String displayName = itemMeta.getDisplayName();
            for (String key : map.keySet()){
                displayName = displayName.replace(key, map.get(key));
            }
            itemMeta.setDisplayName(displayName);
        }
        if (itemMeta.hasLore()){
            List<String> loreList = itemMeta.getLore();
            for (int i = 0; i < loreList.size(); ++i){
                String lore = loreList.get(i);
                for (String key : map.keySet()){
                    lore = lore.replace(key, map.get(key));
                    loreList.set(i, lore);
                }
            }
            itemMeta.setLore(loreList);
        }
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(amount);
        return itemStack;
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }


}
