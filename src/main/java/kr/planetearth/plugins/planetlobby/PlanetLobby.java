package kr.planetearth.plugins.planetlobby;

import kr.planetearth.plugins.planetlobby.listeners.*;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class PlanetLobby extends JavaPlugin {
    private static final String DEFAULT_LANG = "en_US";
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private Map < String, YamlConfiguration > languageFiles;
    private Logger logger;

    @Override
    public void onEnable() {
        logger = this.getLogger();

        loadLanguageFiles();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        logger.info("PlanetLobby has been disabled.");
    }

    private void loadLanguageFiles() {
        languageFiles = new HashMap<> ();
        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
            saveResource("lang/en_US.yml", false);
            saveResource("lang/ko_KR.yml", false);
        }
        File[] files = langFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file: files) {
                String langCode = file.getName().replace(".yml", "");
                languageFiles.put(langCode, YamlConfiguration.loadConfiguration(file));
            }
        }
    }

    public Component getMessage(String key, Player player, TagResolver...resolvers) {
        String langCode = (player != null && player.locale().toString().startsWith("ko")) ? "ko_KR" : "en_US";
        YamlConfiguration config = languageFiles.getOrDefault(langCode, languageFiles.get(DEFAULT_LANG));
        String rawMessage = config.getString(key, "<red>Message not found: " + key + "</red>");
        return MINI_MESSAGE.deserialize(rawMessage, resolvers);
    }

    @Override
    public void onDisable() {
        logger.info("PlanetLobby has been disabled.");
    }
}
