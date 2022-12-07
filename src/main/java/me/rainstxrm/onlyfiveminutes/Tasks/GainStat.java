package me.rainstxrm.onlyfiveminutes.Tasks;

import me.rainstxrm.onlyfiveminutes.OnlyFiveMinutes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GainStat implements Listener {

    //TASK: Lose x amount of health
    //Goal: 30 * amount of  players
    //TAG: healHealth

    static OnlyFiveMinutes plugin = OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class);

    int healthGoal = 0;
    double healthTotal = 0;

    @EventHandler
    public void onHealthGain(EntityRegainHealthEvent e){

        if (plugin.manager.activeTasks.contains("healHealth") && !plugin.manager.completed.contains("healHealth")){
            if (healthGoal == 0){
                healthGoal = 30 * plugin.manager.players.size();
            }
            if (e.getEntity() instanceof Player){
                if (plugin.manager.players.contains(e.getEntity().getUniqueId())){
                    healthTotal += e.getAmount();
                    if (healthTotal >= healthGoal){
                        plugin.manager.completeTask("healHealth", e.getEntity().getUniqueId(), healthGoal);
                        healthTotal = 0;
                        healthGoal = 0;
                    }
                }
            }
        }
    }

    @EventHandler
    public void resetTotal(PlayerMoveEvent e){
        if (!plugin.manager.getState()){
            healthTotal = 0;
        }
    }

}
