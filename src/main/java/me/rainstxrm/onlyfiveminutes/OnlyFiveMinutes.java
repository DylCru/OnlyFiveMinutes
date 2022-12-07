package me.rainstxrm.onlyfiveminutes;

import me.rainstxrm.onlyfiveminutes.Tasks.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class OnlyFiveMinutes extends JavaPlugin {

    Logger log = this.getLogger();
    public GameManager manager = new GameManager();

    @Override
    public void onEnable() {
        log.log(Level.INFO, "Plugin has been enabled");
        HandyItems.init();
        getCommand("fivemins").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new NetherTravel(), this);
        getServer().getPluginManager().registerEvents(new LoseStat(), this);
        getServer().getPluginManager().registerEvents(new GainStat(), this);
        getServer().getPluginManager().registerEvents(new MineBlock(), this);
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
        getServer().getPluginManager().registerEvents(new KillMobs(), this);
   }

    @Override
    public void onDisable() {
        log.log(Level.INFO, "Plugin has been disabled");
        manager.changeState();
    }
}
