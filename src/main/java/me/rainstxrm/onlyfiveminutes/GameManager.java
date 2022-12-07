package me.rainstxrm.onlyfiveminutes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameManager {

    boolean gameActive;
    public List<UUID> players = new ArrayList<>();

    String[] taskTags = {"enterNether", "chopWood", "killFarm", "loseHealth", "loseHunger", "healHealth", "drown", "mineStone"};
    public List<String> activeTasks = new ArrayList<>();
    public List<String> completed = new ArrayList<>();

    HashMap<String, String> taskDesc = new HashMap<>();

    public GameManager(){
        this.gameActive = false;
    }

    void makeDescriptions(){
        taskDesc.put("enterNether", "Travel to the overworld FROM the nether");
        taskDesc.put("killFarm", "Kill [x] Farm animals".replace("[x]", String.valueOf(20 * players.size())));
        taskDesc.put("chopWood", "Chop [x] wood blocks".replace("[x]", String.valueOf(50 * players.size())));
        taskDesc.put("loseHealth", "Lose [x] health".replace("[x]", String.valueOf(30 * players.size())));
        taskDesc.put("loseHunger", "Lose or Gain hunger [x] times".replace("[x]", String.valueOf(15 * players.size())));
        taskDesc.put("healHealth", "Heal [x] health".replace("[x]", String.valueOf(30 * players.size())));
        taskDesc.put("drown", "Drown [x] times".replace("[x]", String.valueOf(3 * players.size())));
        taskDesc.put("mineStone", "Mine [x] stone".replace("[x]", String.valueOf(100 * players.size())));
        addProgressItem();
    }

    public void generateTasks(){
        for (int i = 0; i < 5; i++){
            int taskNum = (int) (Math.random() * taskTags.length);
            if (!activeTasks.contains(taskTags[taskNum])){
                activeTasks.add(taskTags[taskNum]);
            } else {
                i -= 1;
            }
        }
    }

    public void changeState(){
        gameActive = !gameActive;
    }
    public boolean getState(){
        return gameActive;
    }

    public void clearTasks(){
        this.activeTasks.clear();
        this.completed.clear();
    }

    public void addPlayers(UUID player){
        this.players.add(player);
    }

    ItemStack progress;

    private void addProgressItem(){
        progress = new ItemStack(Material.PAPER);
        ItemMeta meta = progress.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Your tasks");
        List<String> lore = new ArrayList<>();
        for (String i : activeTasks){
            lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "✔ " + ChatColor.GRAY + taskDesc.get(i));
        }
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Replacements cost 200 diamonds. Maybe I shouldn't lose this then.");
        meta.setLore(lore);
        progress.setItemMeta(meta);

        for (UUID id : players){
            Bukkit.getPlayer(id).getInventory().setItem(8, progress);
        }
    }

    private void updateProgressItem(){
        ItemStack item = progress;
        ItemMeta meta = item.getItemMeta();
        meta.getLore().clear();
        List<String> lore = new ArrayList<>();
        for (String i : activeTasks){
            if (completed.contains(i)){
                    lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "✔ " + ChatColor.GREEN + taskDesc.get(i));
            } else {
                    lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "✔ " + ChatColor.GRAY + taskDesc.get(i));
            }
        }
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Replacements cost 200 diamonds. Maybe I shouldn't lose this then.");
        meta.setLore(lore);
        item.setItemMeta(meta);

        for (UUID id : players){
            Bukkit.getPlayer(id).getInventory().setItem(8, item);
        }
    }

    public void resetPlayers(){
        this.players.clear();
    }

    public void completeTask(String tag, UUID pId, int goal){
        if (activeTasks.contains(tag)){
            completed.add(tag);
            for (UUID id : players){
                Player player = Bukkit.getPlayer(id);
                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "          TASK COMPLETE");
                player.sendMessage("");
                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "✔ " + ChatColor.GREEN + taskDesc.get(tag).replace("[x]", String.valueOf(goal)));
                player.sendMessage(ChatColor.DARK_GRAY + "        Completed by " + Bukkit.getPlayer(pId).getName());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
                taskProgressBar(player);
            }
            updateProgressItem();

            if (completed.size() == 5){
                win(Bukkit.getPlayer(pId));
            }
        }
    }

    private void taskProgressBar(Player player){
        BossBar bar = Bukkit.createBossBar(ChatColor.GOLD + "" + ChatColor.BOLD + "Completed " + completed.size() + "/5 Tasks", BarColor.RED, BarStyle.SOLID);
        bar.setProgress(completed.size() / 10.0 * 2);
        bar.addPlayer(player);

        new BukkitRunnable(){

            @Override
            public void run() {
                bar.removePlayer(player);
            }
        }.runTaskLater(OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class),20 * 5);
    }

    private void win(Player player){
        for (Player p : Bukkit.getOnlinePlayers()){
            for (UUID id : players){
                p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + Bukkit.getPlayer(id).getDisplayName() + ChatColor.GREEN + " Completed all the tasks!");
            }
        }
        Bukkit.getServer().dispatchCommand(player, "fivemins stop win");
    }

}
