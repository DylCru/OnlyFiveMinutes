package me.rainstxrm.onlyfiveminutes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class HandyItems {

    public static ItemStack hSword;
    public static ItemStack hPick;
    public static ItemStack hAxe;

    public static void init(){
        hSword = createHandyItem("Sword");
        hPick = createHandyItem("Pickaxe");
        hAxe = createHandyItem("Axe");
    }

    private static ItemStack createHandyItem(String name){
        ItemStack item = new ItemStack(Material.valueOf("STONE_"+name.toUpperCase()));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Handy " + name);
        meta.setLore(Collections.singletonList(ChatColor.DARK_GRAY + "A handy item to help you complete your tasks!"));
        meta.addEnchant(Enchantment.DURABILITY, 2, true);
        if (name.equalsIgnoreCase("sword")){
            meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        } else {
            meta.addEnchant(Enchantment.DIG_SPEED, 2, true);
        }
        item.setItemMeta(meta);
        return item;
    }
}
