package me.rainstxrm.onlyfiveminutes.Tasks;

import me.rainstxrm.onlyfiveminutes.OnlyFiveMinutes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MineBlock implements Listener {

    //TASK: Chop X amount of wood blocks
    //Goal: 50 * amount of  players
    //TAG: chopWood

    int woodGoal = 0;
    int woodTotal = 0;

    static OnlyFiveMinutes plugin = OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class);

    @EventHandler
    public void onWoodBreak(BlockBreakEvent e){
        if (woodGoal == 0){
            woodGoal = 50 * plugin.manager.players.size();
        }

        if (plugin.manager.activeTasks.contains("chopWood") && !plugin.manager.completed.contains("chopWood")){
            if (plugin.manager.players.contains(e.getPlayer().getUniqueId())){
                if (e.getBlock().getType().toString().contains("LOG")){
                    woodTotal++;

                    if (woodTotal >= woodGoal){
                        plugin.manager.completeTask("chopWood", e.getPlayer().getUniqueId(), woodTotal);
                        woodTotal = 0;
                        woodGoal = 0;
                    }
                }
            }
        }
    }

    @EventHandler
    public void resetTotal(PlayerMoveEvent e){
        if (!plugin.manager.getState()){
            woodTotal = 0;
            stoneTotal = 0;
        }
    }


    //TASK: Mine x amount of stone
    //Goal: 100 * amount of  players
    //TAG: mineStone

    int stoneTotal = 0;
    int stoneGoal = 0;

    @EventHandler
    public void onStoneBreak(BlockBreakEvent e){
        if (stoneGoal == 0){
            stoneGoal = 100 * plugin.manager.players.size();
        }

        if (plugin.manager.activeTasks.contains("mineStone") && !plugin.manager.completed.contains("mineStone")){
            if (plugin.manager.players.contains(e.getPlayer().getUniqueId())){
                if (e.getBlock().getType() == Material.STONE){
                    stoneTotal++;
                    if (stoneTotal >= stoneGoal){
                        plugin.manager.completeTask("mineStone", e.getPlayer().getUniqueId(), stoneTotal);
                        stoneTotal = 0;
                        stoneGoal = 0;
                    }
                }
            }
        }
    }
}
