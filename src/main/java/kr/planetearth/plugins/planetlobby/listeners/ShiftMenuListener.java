package kr.planetearth.plugins.planetlobby.listeners;

import kr.planetearth.plugins.planetlobby.PlanetLobby;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.List;
import java.util.UUID;

public class ShiftMenuListener implements Listener {
  private final PlanetLobby plugin;

  public ShiftMenuListener(PlanetLobby plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
    Player player = event.getPlayer();

    if (player.isSneaking()) {
      event.setCancelled(true);
      player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
      openMenu(player);
    }
  }

  private void openMenu(Player player) {
    Component titleComponent = plugin.getMessage("shift_menu_title", player);
    String title = PlainTextComponentSerializer.plainText().serialize(titleComponent);

    ChestGui gui = new ChestGui(1, title);
    gui.setOnGlobalClick(event -> event.setCancelled(true));

    StaticPane pane = new StaticPane(0, 0, 9, 1);

    addMenuItem(pane, player, 4, Material.PLAYER_HEAD, "menu_1_name", "menu_1_description", "ajqueue join planetearth", "customskull:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFkZDRmZTRhNDI5YWJkNjY1ZGZkYjNlMjEzMjFkNmVmYTZhNmI1ZTdiOTU2ZGI5YzVkNTljOWVmYWIyNSJ9fX0=");

    gui.addPane(pane);
    gui.show(player);
  }

  private void addMenuItem(StaticPane pane, Player player, int slot, Material material, String nameKey, String descriptionKey, String command, String skullOwner) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();

    if (meta != null) {
      if (meta instanceof SkullMeta && skullOwner != null) {
        SkullMeta skullMeta = (SkullMeta) meta;

        if (skullOwner.startsWith("customskull:")) {
          String texture = skullOwner.substring("customskull:".length());
          PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
          profile.setProperty(new ProfileProperty("textures", texture));
          skullMeta.setPlayerProfile(profile);
        } else {
          skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(skullOwner));
        }
      }

      Component displayName = plugin.getMessage(nameKey, player);
      meta.displayName(displayName);

      List <Component> lore = plugin.getMessageList(descriptionKey, player);
      meta.lore(lore);

      item.setItemMeta(meta);
    }

    GuiItem guiItem = new GuiItem(item, event -> {
      event.setCancelled(true);
      Player clickedPlayer = (Player) event.getWhoClicked();
      clickedPlayer.closeInventory();
      clickedPlayer.performCommand(command);
    });

    pane.addItem(guiItem, slot, 0);
  }
}
