package me.rainstxrm.onlyfiveminutes.Tasks;

import me.rainstxrm.onlyfiveminutes.OnlyFiveMinutes;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class KillMobs implements Listener {

    //Task: Kill x farm animals
    //Goal: 20 * player count
    //Tag: killPigs

    int farmTotal = 0;
    int farmGoal = 0;

    static OnlyFiveMinutes plugin = OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class);

    @EventHandler
    public void onKillfarm(EntityDeathEvent e){
        if (farmGoal == 0){
            farmGoal = 20 * plugin.manager.players.size();
        }

        if (e.getEntity().getType().equals(EntityType.PIG) || e.getEntity().getType().equals(EntityType.SHEEP) || e.getEntity().getType().equals(EntityType.COW) || e.getEntity().getType().equals(EntityType.CHICKEN)){
            if (plugin.manager.activeTasks.contains("killFarm") && !plugin.manager.completed.contains("killFarm")){
                if (plugin.manager.players.contains(e.getEntity().getKiller().getUniqueId())){
                    farmTotal++;
                    if (farmTotal >= farmGoal){
                        plugin.manager.completeTask("killFarm", e.getEntity().getKiller().getUniqueId(), farmGoal);
                        farmTotal = 0;
                        farmGoal = 0;
                    }
                }
            }
        }
    }

    @EventHandler
    public void resetTotal(PlayerMoveEvent e){
        if (!plugin.manager.getState()){
            farmTotal = 0;
        }
    }

}
