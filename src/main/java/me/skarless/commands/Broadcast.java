package me.skarless.commands;

import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Broadcast extends SmpCommand {
    public Broadcast(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(StringParse.getMessage("Broadcast.NoMessage"));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        for (final String s : args) {
            sb.append(s).append(" ");
        }
        for (final Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(StringParse.getMessage("Broadcast.Prefix"), ChatColor.translateAlternateColorCodes('&', sb.toString()));
        }
        Bukkit.getConsoleSender().sendMessage(StringParse.getMessage("Broadcast.Prefix") + ChatColor.translateAlternateColorCodes('&', sb.toString()));
    }
}
