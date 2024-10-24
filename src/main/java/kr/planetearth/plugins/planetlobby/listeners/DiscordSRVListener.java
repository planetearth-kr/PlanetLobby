package kr.planetearth.plugins.planetlobby.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import static net.kyori.adventure.text.logger.slf4j.ComponentLogger.logger;

public class DiscordSRVListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST && event.getKickMessage().contains("NOT_IN_DISCORD")) {

            String playerUUID = event.getUniqueId().toString();

            DiscordSRV.getPlugin().getAccountLinkManager().unlink(playerUUID);

            logger().info("Unlinked Discord account for player {} because they were kicked for 'Not in server'.", event.getName());
        }
    }

}
