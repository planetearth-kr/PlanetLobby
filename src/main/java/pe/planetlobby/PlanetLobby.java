package pe.planetlobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pe.planetlobby.listener.PlayerListener;

public final class PlanetLobby extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
