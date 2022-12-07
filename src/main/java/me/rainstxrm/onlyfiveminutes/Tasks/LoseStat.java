package me.rainstxrm.onlyfiveminutes.Tasks;

import me.rainstxrm.onlyfiveminutes.OnlyFiveMinutes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class LoseStat implements Listener {

    //TASK: Lose x amount of health
    //Goal: 30 * amount of  players
    //TAG: loseHealth loseHunger

    static OnlyFiveMinutes plugin = OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class);

    int healthGoal = 0;
    int healthTotal = 0;

    @EventHandler
    public void onHealthLoss(EntityDamageEvent e){

        if (plugin.manager.activeTasks.contains("loseHealth") && !plugin.manager.completed.contains("loseHealth")){
            if (healthGoal == 0){
                healthGoal = 30 * plugin.manager.players.size();
            }
            if (e.getEntity() instanceof Player){
                if (plugin.manager.players.contains(e.getEntity().getUniqueId())){
                    healthTotal += e.getFinalDamage();
                    if (healthTotal >= healthGoal){
                        plugin.manager.completeTask("loseHealth", e.getEntity().getUniqueId(), healthGoal);
                        healthTotal = 0;
                    }
                }
            }
        }
    }

    @EventHandler
    public void resetTotal(PlayerMoveEvent e){
        if (!plugin.manager.getState()){
            healthTotal = 0;
            foodTotal = 0;
        }
    }

    //TASK: Lose x amount of hunger
    //Goal: 15 * amount of  players
    //TAG: loseHunger

    int foodGoal = 0;
    double foodTotal = 0;

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent e){
        if (plugin.manager.activeTasks.contains("loseHunger") && !plugin.manager.completed.contains("loseHunger")){
            if (foodGoal == 0){
                foodGoal = 15 * plugin.manager.players.size();
            }
            if (e.getEntity() instanceof Player){
                if (plugin.manager.players.contains(e.getEntity().getUniqueId())){
                    foodTotal += 0.5;
                    if (foodTotal >= foodGoal){
                        plugin.manager.completeTask("loseHunger", e.getEntity().getUniqueId(), foodGoal);
                        foodTotal = 0;
                        foodGoal = 0;
                    }
                }
            }
        }
    }


}
