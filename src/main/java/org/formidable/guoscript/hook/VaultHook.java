package org.formidable.guoscript.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private static Economy economy = null;

    private static void setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    public static double getMoney(Player player){
        if (economy == null){
            setupEconomy();
        }
        return economy.getBalance(player);
    }

    public static void addMoney(Player player, double amount){
        if (economy == null){
            setupEconomy();
        }
        if (amount > 0){
            economy.depositPlayer(player, amount);
        }else {
            economy.withdrawPlayer(player, -amount);
        }
    }
}
