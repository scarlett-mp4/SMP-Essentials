package me.skarless.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.skarless.Smp;
import org.apache.commons.text.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

public class ChatListener implements Listener {
    @EventHandler
    public void onChatMessage(final AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        for (final String s : Smp.getInstance().getConfig().getStringList("Chat.BlockedWords")) {
            if (e.getMessage().toLowerCase().contains(s.toLowerCase())) {
                if (Smp.getInstance().getConfig().getString("Chat.BlockMessage").equals("")) {
                    return;
                }
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Smp.getInstance().getConfig().getString("Chat.BlockMessage")));
                return;
            }
        }
        final Player p = e.getPlayer();
        String raw = "";
        final Pattern DISPLAYNAME = Pattern.compile("[DISPLAYNAME]", 16);
        final Pattern PLAYER = Pattern.compile("[PLAYER]", 16);
        final Pattern MESSAGE = Pattern.compile("[MESSAGE]", 16);
        String message = Smp.getInstance().getConfig().getString("Chat.Format");
        if (p.hasPermission("smp.chat.color")) {
            raw = ChatColor.translateAlternateColorCodes('&', StringEscapeUtils.escapeJava(e.getMessage()));
        } else {
            raw = StringEscapeUtils.escapeJava(e.getMessage());
        }
        assert message != null;
        message = DISPLAYNAME.matcher(message).replaceAll(p.getDisplayName());
        message = PLAYER.matcher(message).replaceAll(p.getName());
        message = MESSAGE.matcher(message).replaceAll(raw);
        message = PlaceholderAPI.setPlaceholders(p, message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        for (Player bp : Bukkit.getOnlinePlayers())
            bp.sendMessage(message);
    }
}
