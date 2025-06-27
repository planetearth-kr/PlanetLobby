package kr.planetearth.plugins.planetlobby.listeners;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.time.Duration;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + player.getName());
        player.getInventory().clear();

        int playerVersion = Via.getAPI().getPlayerVersion(player.getUniqueId());
        if (playerVersion > ProtocolVersion.v1_20_3.getVersion()) {
            Title title = Title.title(
                    Component.text("⚠ 주의").color(NamedTextColor.RED).decorate(TextDecoration.BOLD),
                    Component.text("권장 버전은 1.20.1~1.20.4 입니다.").color(NamedTextColor.WHITE),
                    Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(15), Duration.ofSeconds(1))
            );
            player.showTitle(title);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            player.performCommand("bs menu");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        victim.getInventory().clear();
        
        Player killer = victim.getKiller();
        if (killer != null) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
        }
    }
}
