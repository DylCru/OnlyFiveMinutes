package me.rainstxrm.onlyfiveminutes;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class Commands implements CommandExecutor {

    static OnlyFiveMinutes plugin = OnlyFiveMinutes.getPlugin(OnlyFiveMinutes.class);
    HashMap<UUID, Location> pLocs = new HashMap<>();
    HashMap<UUID, ItemStack[]> pInvs = new HashMap<>();

    public static Location cLoc;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            plugin.getLogger().log(Level.WARNING, "Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getLabel().equalsIgnoreCase("fivemins")){
            if (args.length == 0){
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "/fivemins [start] [stop] [newloc]");
                return true;
            }
            if (args[0].equalsIgnoreCase("start")){
                if (!player.hasPermission("fivemins.start")){
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command. If you think this is a mistake contact a server admin.");
                    return true;
                }

                if (plugin.manager.getState()){
                    player.sendMessage(ChatColor.RED + "The challenge is already active!");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return true;
                }
                if (player.getWorld().getName().contains("nether")){
                    player.sendMessage(ChatColor.RED + "You can only start challenges in the overworld!");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return true;
                }
                plugin.manager.generateTasks();

                plugin.getLogger().log(Level.INFO, "The challenge is being started! Please avoid reloading or restarting the server until the challenge is over!");


                int randX = (int) (Math.random() * ((int) player.getLocation().getX() + 50000));
                int randZ = (int) (Math.random() * ((int) player.getLocation().getZ() + 50000));
                for (Player p : Bukkit.getOnlinePlayers()){
                    if (p.getWorld().getName().contains("nether")){
                        continue;
                    }
                    p.sendMessage(ChatColor.GREEN + "A five minute challenge is starting! You are being safely teleported to a random location!");
                    p.sendMessage(ChatColor.GREEN + "Your original location and inventory has been saved and you will be teleported back once the challenge has ended");

                    pLocs.put(p.getUniqueId(), p.getLocation());

                    pInvs.put(p.getUniqueId(), p.getInventory().getContents());
                    p.getInventory().clear();

                    cLoc = safeLoc(p.getWorld(), randX, player.getLocation().getBlockY(), randZ);

                    p.teleport(cLoc);
                    p.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20 * 3, 1));
                    p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20 * 10, 20));
                    plugin.manager.addPlayers(p.getUniqueId());
                }
                plugin.manager.makeDescriptions();

                new BukkitRunnable(){

                    @Override
                    public void run() {
                        displayTasks();
                    }
                }.runTaskLater(plugin, 20 * 3);


                plugin.manager.changeState();
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        for (UUID id : plugin.manager.players){
                            Player player1 = Bukkit.getPlayer(id);
                            TimerBar bar = new TimerBar(player1, player1 == player);
                            player1.sendMessage(ChatColor.GREEN + "Challenge started! Good Luck!");
                            player1.sendMessage(ChatColor.GREEN + "You were given a handy item to help you!");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                            switch ((int) (Math.random() * 3)){
                                case 0:
                                    player1.getInventory().addItem(HandyItems.hPick);
                                    break;
                                case 1:
                                    player1.getInventory().addItem(HandyItems.hSword);
                                    break;
                                case 2:
                                    player1.getInventory().addItem(HandyItems.hAxe);
                                    break;
                            }
                        }
                    }
                }.runTaskLater(plugin, 20 * 10);


            }
            if (args[0].equalsIgnoreCase("stop")){
                if (!player.hasPermission("fivemins.stop")){
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command. If you think this is a mistake contact a server admin.");
                    return true;
                }


                if (!plugin.manager.getState()){
                    player.sendMessage(ChatColor.RED + "The challenge hasn't started yet!");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return true;
                }
                for (UUID id : plugin.manager.players){
                    Player player1 = Bukkit.getPlayer(id);
                    if (args.length == 1){
                        player1.sendMessage(ChatColor.RED + "The challenge was stopped!");
                    }

                    if (args.length >= 2){
                        if (args[1].equalsIgnoreCase("win")){
                            player1.sendMessage(ChatColor.GREEN + "Congratulations on winning the challenge!");
                        }
                    }

                    player1.sendMessage(ChatColor.RED + "Sending you back to your original location!");
                    player1.getInventory().clear();

                    player1.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20 * 6, 1));
                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            player1.teleport(pLocs.get(id));
                            player1.getInventory().setContents(pInvs.get(player1.getUniqueId()));
                            player1.playSound(player1.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        }
                    }.runTaskLater(plugin, 20 * 5);
                }
                plugin.manager.changeState();
                plugin.manager.resetPlayers();
                plugin.manager.clearTasks();
            }
            if (args[0].equalsIgnoreCase("newloc")){
                if (!player.hasPermission("fivemins.newloc")){
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command. If you think this is a mistake contact a server admin.");
                    return true;
                }
                if (!plugin.manager.getState()){
                    player.sendMessage(ChatColor.RED + "The challenge hasn't started yet!");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return true;
                }

                int randX = (int) (Math.random() * ((int) player.getLocation().getX() + 50000));
                int randZ = (int) (Math.random() * ((int) player.getLocation().getZ() + 50000));
                cLoc = safeLoc(player.getWorld(), randX, player.getLocation().getBlockY(), randZ);

                for (UUID pl : plugin.manager.players){
                    Player p = Bukkit.getPlayer(pl);
                    p.sendMessage(ChatColor.GREEN + "Your challenge location is being redone. Sending you there now.");
                    p.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20 * 3, 1));
                    p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20 * 10, 20));
                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            p.teleport(cLoc);
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        }
                    }.runTaskLater(plugin, 20 * 2);
                }
            }
        }
        return true;
    }

    public void displayTasks(){
        new BukkitRunnable(){
            int count = 0;
            @Override
            public void run() {
                String task = plugin.manager.activeTasks.get(count);
                for (UUID id : plugin.manager.players){
                    Player player = Bukkit.getPlayer(id);


                    player.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "TASK " + (count + 1), ChatColor.GREEN + plugin.manager.taskDesc.get(task), 0, 30, 0);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.4f);
                }
                count++;
                if (count == 5){
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20 ,20);
    }

    private Location safeLoc(World world, int x, int y, int z){
        Location loc = new Location(world,x,y,z);

        while (loc.getBlock().getType() != Material.AIR){
            loc.add(0,1,0);
        }

        if (loc.getBlock().getType() == Material.WATER){
            loc.clone().subtract(0,2,0).getBlock().setType(Material.OBSIDIAN);
        }

        return loc;
    }
}
