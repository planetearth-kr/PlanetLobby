package kr.planetearth.plugins.planetlobby.listeners;

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
        if (e.getPlayer().hasPlayedBefore()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + e.getPlayer().getName());
            e.getPlayer().getInventory().clear();
        }
        else{
            new BukkitRunnable(){
                @Override
                public void run() {
                    e.getPlayer().performCommand("/server planetearth");
                }
            }.runTaskLater(PlanetLobby.getInstance(), 20);
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
