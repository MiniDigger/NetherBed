package me.minidigger.netherbed;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class NetherBed extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        CustomBed_v1_10_R1.register();
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
