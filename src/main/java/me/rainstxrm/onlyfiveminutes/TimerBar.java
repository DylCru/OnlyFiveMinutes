package me.rainstxrm.onlyfiveminutes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TimerBar {

    Player player;
    BossBar bar;
    static OnlyFiveMinutes plugin = OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class);

    boolean started;

    public TimerBar(Player player, boolean started){
        this.player = player;
        this.started = started;
        bar = Bukkit.createBossBar(ChatColor.BLUE + "" + ChatColor.BOLD + "Time remaining: 5:00", BarColor.PURPLE, BarStyle.SEGMENTED_10);
        bar.addPlayer(player);
        countdown();
    }


    int seconds = 0;
    int mins = 0;
    int timer = 0;
    private void countdown(){
        new BukkitRunnable(){
            @Override
            public void run() {
                if (mins == 5){
                    seconds = 0;
                    mins = 0;
                    timer = 0;
                    bar.removePlayer(player);

                    if (started){
                        Bukkit.getServer().dispatchCommand(player, "fivemins stop time");
                    }

                    plugin.manager.changeState();
                    plugin.manager.clearTasks();
                    plugin.manager.resetPlayers();
                    player.sendMessage(ChatColor.RED + "Time has run out!");

                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_WHINE, 1.0f, 1.0f);
                    cancel();
                }
                if (!plugin.manager.getState()){
                    bar.removePlayer(player);
                    cancel();
                }

                bar.setProgress(1 - (timer / 300.0));

                seconds++;
                timer++;
                if (seconds == 60){
                    seconds = 0;
                    mins++;
                    player.sendMessage(ChatColor.RED + "" + (5 - mins) + " Minutes remaining");
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                }




                if (seconds >= 50){
                    bar.setTitle(ChatColor.BLUE + "" + ChatColor.BOLD + "Time remaining: " + (4 - mins) + ":0" + (59 - seconds));
                } else {
                    bar.setTitle(ChatColor.BLUE + "" + ChatColor.BOLD + "Time remaining: " + (4 - mins) + ":" + (59 - seconds));
                }
            }
        }.runTaskTimer(plugin,0, 20);
    }
}
