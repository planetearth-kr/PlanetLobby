package kr.planetearth.plugins.planetlobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import kr.planetearth.plugins.planetlobby.listeners.PlayerListener;
import kr.planetearth.plugins.planetlobby.listeners.DiscordSRVListener;

public final class PlanetLobby extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DiscordSRVListener(), this);
    }

    @Override
    public void onDisable() {
    }
}
