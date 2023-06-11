package me.skarless.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.skarless.Smp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.LocalDateTime;

import java.util.regex.Pattern;

public class LoginListener implements Listener {
    @EventHandler
    public void onLogin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final FileConfiguration c = Smp.getInstance().saveConfig.getConfig();
        if (!c.contains(p.getUniqueId().toString())) {
            c.set("PlayerCount", c.getInt("PlayerCount") + 1);
            c.set(p.getUniqueId() + ".id", c.getInt("PlayerCount"));
            c.set(p.getUniqueId() + ".firstjoin", LocalDateTime.now().toString());
            Bukkit.getScheduler().runTaskLater(Smp.getInstance(), () -> {
                try {
                    Location loc = new Location(Bukkit.getWorld(c.getString("Spawn.World")), c.getDouble("Spawn.X"), c.getDouble("Spawn.Y"), c.getDouble("Spawn.Z"), (float) c.getDouble("Spawn.Yaw"), (float) c.getDouble("Spawn.Pitch"));
                    if (!p.hasPlayedBefore()) {
                        p.teleport(loc);
                    }
                } catch (Exception ex) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "A user attempted to teleport to spawn, however, there is no spawn!");
                }
            }, 5L);
        }
        c.set(p.getUniqueId() + ".username", p.getName());
        c.set(p.getUniqueId() + ".lastseen", LocalDateTime.now().toString());
        c.set(p.getUniqueId() + ".ip", p.getAddress().getAddress().toString());
        c.set(p.getUniqueId() + ".nick", p.getName());
        Smp.getInstance().saveConfig.saveConfig();
        if (!c.getString(p.getUniqueId() + ".nick").equals(p.getName())) {
            p.setDisplayName(c.getString(p.getUniqueId() + ".nick"));
            p.setPlayerListName(c.getString(String.valueOf(p.getUniqueId() + ".nick")));
        }
        final Pattern DISPLAYNAME = Pattern.compile("[DISPLAYNAME]", 16);
        String message = PlaceholderAPI.setPlaceholders(p, Smp.getInstance().getConfig().getString("Login.JoinMessage"));
        message = DISPLAYNAME.matcher(message).replaceAll(e.getPlayer().getDisplayName());
        message = ChatColor.translateAlternateColorCodes('&', message);
        e.setJoinMessage(message);
    }

    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        final Pattern DISPLAYNAME = Pattern.compile("[DISPLAYNAME]", 16);
        String message = PlaceholderAPI.setPlaceholders(e.getPlayer(), Smp.getInstance().getConfig().getString("Login.LeaveMessage"));
        message = DISPLAYNAME.matcher(message).replaceAll(e.getPlayer().getDisplayName());
        message = ChatColor.translateAlternateColorCodes('&', message);
        e.setQuitMessage(message);
    }
}
