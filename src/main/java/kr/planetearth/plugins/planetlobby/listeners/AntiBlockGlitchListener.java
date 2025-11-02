package kr.planetearth.plugins.planetlobby.listeners;

import kr.planetearth.plugins.planetlobby.PlanetLobby;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import java.util.HashSet;
import java.util.Set;

public class AntiBlockGlitchListener implements Listener {
  private final PlanetLobby plugin;

  public AntiBlockGlitchListener(PlanetLobby plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
  public void onBlockPlace(BlockPlaceEvent event) {
    if (!event.isCancelled()) {
      return;
    }

    Player player = event.getPlayer();
    Block block = event.getBlock();
    Location playerLocation = player.getLocation();

    double xDiff = block.getX() - playerLocation.getX();
    double zDiff = block.getZ() - playerLocation.getZ();

    boolean isPotentiallyGlitching = (xDiff <= 0.3 && xDiff >= -1.3)
      && (zDiff <= 0.3 && zDiff >= -1.3)
      && (block.getY() <= playerLocation.getBlockY());

    if (!isPotentiallyGlitching) {
      return;
    }

    BlockData originalBlockData = block.getBlockData();
    try {
      if (originalBlockData instanceof Slab && ((Slab) originalBlockData).getType() == Slab.Type.DOUBLE) {
        BlockData bottomSlab = originalBlockData.clone();
        ((Slab) bottomSlab).setType(Slab.Type.BOTTOM);
        block.setBlockData(bottomSlab, false);
      } else {
        block.setType(Material.AIR, false);
      }

      Set<Block> firstBlocks = getFloorBlocks(playerLocation, player.getBoundingBox());
      Set<Block> secondBlocks = new HashSet<>();
      if (getLowerBlocks(firstBlocks, secondBlocks)) {
        Set<Block> thirdBlocks = new HashSet<>();
        if (getLowerBlocks(secondBlocks, thirdBlocks)) {
          double groundY = getMaxY(thirdBlocks);
          playerLocation.setY(playerLocation.getBlockY() - 2 + groundY);
          player.teleport(playerLocation);
        }
      }
    } finally {
      block.setBlockData(originalBlockData, false);
    }
  }

  private Set<Block> getFloorBlocks(Location location, BoundingBox boundingBox) {
    Set<Block> floorBlocks = new HashSet<>();
    World world = location.getWorld();
    int playerY = location.getBlockY();

    int blx = NumberConversions.floor(boundingBox.getMinX());
    int bgx = NumberConversions.ceil(boundingBox.getMaxX());
    int blz = NumberConversions.floor(boundingBox.getMinZ());
    int bgz = NumberConversions.ceil(boundingBox.getMaxZ());

    for (int x = blx; x < bgx; x++) {
      for (int z = blz; z < bgz; z++) {
        floorBlocks.add(world.getBlockAt(x, playerY, z));
      }
    }
    return floorBlocks;
  }

  private boolean getLowerBlocks(Set<Block> sourceBlocks, Set<Block> destinationBlocks) {
    for (Block b : sourceBlocks) {
      if (!b.isPassable()) {
        return false;
      }
      destinationBlocks.add(b.getRelative(BlockFace.DOWN));
    }
    return true;
  }

  private double getMaxY(Set<Block> blocks) {
    double y = 0;
    for (Block b : blocks) {
      double maxY = b.getCollisionShape().getBoundingBoxes().stream()
        .mapToDouble(BoundingBox::getMaxY)
        .max()
        .orElse(1.0);
      if (maxY > y) {
        y = maxY;
      }
    }
    return y;
  }
}
