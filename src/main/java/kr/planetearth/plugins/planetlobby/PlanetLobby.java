package kr.planetearth.plugins.planetlobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import kr.planetearth.plugins.planetlobby.listeners.PlayerListener;

public final class PlanetLobby extends JavaPlugin {
    private static PlanetLobby instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static PlanetLobby getInstance() {
        return instance;
    }
}
