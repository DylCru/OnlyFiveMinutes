package me.rainstxrm.onlyfiveminutes.Tasks;

import me.rainstxrm.onlyfiveminutes.Commands;
import me.rainstxrm.onlyfiveminutes.OnlyFiveMinutes;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathEvent implements Listener {

    //Task: Drown x times
    //Goal: 3 * player count
    //Tag: drown

    static OnlyFiveMinutes plugin = OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class);

    int drownTotal = 0;
    int drownGoal = 0;

    ItemStack[] pInv;

    @EventHandler
    public void onDrown(EntityDeathEvent e){
        if (drownGoal == 0){
            drownGoal = 3 * plugin.manager.players.size();
        }

        if (plugin.manager.activeTasks.contains("drown") && !plugin.manager.completed.contains("drown")){
            if (e.getEntity() instanceof Player){
                if (plugin.manager.players.contains(e.getEntity().getUniqueId())){
                    EntityDamageEvent ed = e.getEntity().getLastDamageCause();
                    EntityDamageEvent.DamageCause dc = ed.getCause();

                    if (dc.equals(EntityDamageEvent.DamageCause.DROWNING)){
                        drownTotal++;
                        if (drownTotal >= drownGoal){
                            plugin.manager.completeTask("drown", e.getEntity().getUniqueId(), drownGoal);
                            drownTotal = 0;
                            drownGoal = 0;
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void saveDrops(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Player)){
            return;
        }

        if (plugin.manager.players.contains(e.getEntity().getUniqueId())){
            pInv = e.getDrops().toArray(new ItemStack[0]);
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void resetTotal(PlayerMoveEvent e){
        if (!plugin.manager.getState()){
            drownTotal = 0;
        }
    }


    @EventHandler
    public void respawn(PlayerRespawnEvent e){
        if (plugin.manager.players.contains(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(ChatColor.GREEN + "Sending you back to your challenge location!");
            e.getPlayer().addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(160, 10));
            e.getPlayer().getInventory().setContents(pInv);
            new BukkitRunnable(){

                @Override
                public void run() {
                    e.getPlayer().teleport(Commands.cLoc);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }
            }.runTaskLater(plugin, 40);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if (plugin.manager.players.contains(e.getPlayer().getUniqueId())){
            e.getPlayer().sendMessage(ChatColor.RED + "Please avoid leaving the server while the challenge is active. The challenge is still active");
        }
    }
}
