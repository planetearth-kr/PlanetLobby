package kr.planetearth.plugins.planetlobby.listeners;

import kr.planetearth.plugins.planetlobby.PlanetLobby;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.time.Duration;
import java.time.Instant;

public class PlayerListener implements Listener {
    private final PlanetLobby plugin;

    public PlayerListener(PlanetLobby plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getWorld("world");
        Location spawn = new Location(world, 866.0, 18.0, 373.5, 90.0f, 0.0f);

        player.teleport(spawn);
        player.getInventory().clear();

        int playerVersion = Via.getAPI().getPlayerVersion(player.getUniqueId());
        if (playerVersion > ProtocolVersion.v1_20_3.getVersion()) {
            Component titleComponent = plugin.getMessage("version_warning_title", player);
            Component subtitleComponent = plugin.getMessage("version_warning_subtitle", player);

            Title title = Title.title(
                titleComponent,
                subtitleComponent,
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(10), Duration.ofSeconds(1))
            );
            player.showTitle(title);
        }

        if (!player.hasPlayedBefore()) {
            LuckPerms luckPerms = LuckPermsProvider.get();

            luckPerms.getUserManager().loadUser(player.getUniqueId()).thenAccept(user -> {
                if (user != null) {
                    InheritanceNode newbieGroup = InheritanceNode.builder("newbie")
                        .expiry(Instant.now().plus(Duration.ofDays(10)))
                        .build();
                    user.data().add(newbieGroup);

                    PermissionNode tutorialPerm = PermissionNode.builder("pecore.tutorial")
                        .expiry(Instant.now().plus(Duration.ofHours(1)))
                        .build();
                    user.data().add(tutorialPerm);

                    PermissionNode priorityPerm = PermissionNode.builder("ajqueue.priority.10")
                        .expiry(Instant.now().plus(Duration.ofHours(1)))
                        .build();
                    user.data().add(priorityPerm);

                    PermissionNode bypassPerm = PermissionNode.builder("ajqueue.joinfullandbypass")
                        .expiry(Instant.now().plus(Duration.ofHours(1)))
                        .build();
                    user.data().add(bypassPerm);

                    luckPerms.getUserManager().saveUser(user);
                }
            });
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            player.performCommand("shop open ServerSelect");
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
