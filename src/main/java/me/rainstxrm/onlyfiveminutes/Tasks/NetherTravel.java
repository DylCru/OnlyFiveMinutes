package me.rainstxrm.onlyfiveminutes.Tasks;

import me.rainstxrm.onlyfiveminutes.OnlyFiveMinutes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class NetherTravel implements Listener {

    //TASK: Enter the Nether
    //Goal: 1 time
    //TAG: enterNether

    String tag = "enterNether";

    static OnlyFiveMinutes plugin = OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class);

    @EventHandler
    public void onNetherEnter(PlayerChangedWorldEvent e){
        if (plugin.manager.activeTasks.contains("enterNether") && !plugin.manager.completed.contains("enterNether")){
            if (plugin.manager.players.contains(e.getPlayer().getUniqueId())){
                if (e.getFrom().getName().equalsIgnoreCase("world_nether")){
                    plugin.manager.completeTask(tag, e.getPlayer().getUniqueId(), 0);
                }
            }
        }
    }
}
