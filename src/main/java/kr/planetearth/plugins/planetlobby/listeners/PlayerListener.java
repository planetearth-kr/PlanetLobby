package kr.planetearth.plugins.planetlobby.listeners;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import kr.planetearth.plugins.planetlobby.PlanetLobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + e.getPlayer().getName());
        e.getPlayer().getInventory().clear();

        int playerVersion = Via.getAPI().getPlayerVersion(e.getPlayer());

        if (playerVersion > ProtocolVersion.v1_20_3.getVersion()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "titlemsg " + e.getPlayer().getName() + " &c&l ⚠ 주의\n&f1.20.1~1.20.4 사이 버전을 사용하실 것을 권장드립니다. -keep:400");
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e){
        Player player = e.getPlayer();
        if (!player.isSneaking())
            return;

        player.performCommand("bs menu");
        e.setCancelled(true);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        if (e.getPlayer().getKiller() == null)return;
        e.getPlayer().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5, 1));
        e.getPlayer().getInventory().clear();
    }
}
