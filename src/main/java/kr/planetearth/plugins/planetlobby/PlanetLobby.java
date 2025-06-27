package kr.planetearth.plugins.planetlobby;

import kr.planetearth.plugins.planetlobby.command.PermissionGivePerUUIDCommand;
import kr.planetearth.plugins.planetlobby.command.RankRewardCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import kr.planetearth.plugins.planetlobby.listeners.PlayerListener;

import java.util.Objects;

public final class PlanetLobby extends JavaPlugin {
    private static PlanetLobby instance;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdirs()) {
                getLogger().warning("Failed to create plugin data folder: " + getDataFolder().getPath());
            }
        }

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Objects.requireNonNull(getCommand("랭크보상")).setExecutor(new RankRewardCommand());
        Objects.requireNonNull(getCommand("PermissionGivePerUUID")).setExecutor(new PermissionGivePerUUIDCommand());
    }

    @Override
    public void onDisable() {
    }

    public static PlanetLobby getInstance(){
        return instance;
    }
}
