package me.skarless.listeners;

import me.skarless.Smp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
        try {
            final Player p = e.getPlayer();
            final FileConfiguration c = Smp.getInstance().saveConfig.getConfig();
            final Location loc = new Location(Bukkit.getWorld(c.getString("Spawn.World")), c.getDouble("Spawn.X"), c.getDouble("Spawn.Y"), c.getDouble("Spawn.Z"), (float) c.getDouble("Spawn.Yaw"), (float) c.getDouble("Spawn.Pitch"));
            if (p.getBedSpawnLocation() != null) {
                return;
            }
            e.setRespawnLocation(loc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
