package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Tpaccept extends SmpCommand {
    public Tpaccept(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Pattern playerPattern = Pattern.compile("[PLAYER]", 16);
        final Pattern timePattern = Pattern.compile("[TIME]", 16);
        final Player p = (Player) sender;
        Player target = null;
        if (args.length == 0) {
            p.sendMessage(StringParse.getMessage("Tpaccept.IncludePlayer"));
            return;
        }
        try {
            if (Bukkit.getPlayer(args[0]).isOnline()) {
                target = Bukkit.getPlayer(args[0]);
            }
        } catch (Exception e) {
            p.sendMessage(StringParse.getMessage("General.PlayerNotOnline"));
            return;
        }
        assert target != null;
        if (!Smp.getInstance().requestList.contains(target.getUniqueId() + " " + p.getUniqueId())) {
            p.sendMessage(StringParse.getMessage("Tpaccept.NoRequest"));
            return;
        }
        p.sendMessage(playerPattern.matcher(StringParse.getMessage("Tpaccept.Accepted")).replaceAll(target.getDisplayName()));
        target.sendMessage(timePattern.matcher(playerPattern.matcher(StringParse.getMessage("Tpaccept.Teleporting")).replaceAll(p.getDisplayName())).replaceAll(String.valueOf(Smp.getInstance().getConfig().getInt("Tpa.WaitTime"))));
        final int x = (int) Math.round(target.getLocation().getX());
        final int y = (int) Math.round(target.getLocation().getY());
        final int z = (int) Math.round(target.getLocation().getZ());
        Player finalTarget = target;
        Bukkit.getScheduler().runTaskLater(Smp.getInstance(), () -> {
            if (x == (int) Math.round(p.getLocation().getX()) && y == (int) Math.round(p.getLocation().getY()) && z == (int) Math.round(p.getLocation().getZ())) {
                p.teleport(finalTarget.getLocation());
                p.sendMessage(StringParse.getMessage("Tpaccept.Teleported"));
            } else {
                p.sendMessage(StringParse.getMessage("Tpaccept.PlayerMoved"));
                finalTarget.sendMessage(StringParse.getMessage("Tpaccept.PlayerMoved"));
            }
            Smp.getInstance().requestList.remove(p.getUniqueId() + " " + finalTarget.getUniqueId());
        }, 20L * Smp.getInstance().getConfig().getInt("Tpa.WaitTime"));
    }
}
