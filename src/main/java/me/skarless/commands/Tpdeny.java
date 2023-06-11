package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Tpdeny extends SmpCommand {
    public Tpdeny(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Pattern playerPattern = Pattern.compile("[PLAYER]", 16);
        final Player p = (Player) sender;
        Player target = null;
        if (args.length == 0) {
            p.sendMessage(StringParse.getMessage("TpDeny.IncludePlayer"));
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
            p.sendMessage(StringParse.getMessage("TpDeny.NoRequest"));
            return;
        }
        Smp.getInstance().requestList.remove(target.getUniqueId() + " " + p.getUniqueId());
        p.sendMessage(playerPattern.matcher(StringParse.getMessage("TpDeny.DenySuccess")).replaceAll(target.getDisplayName()));
        target.sendMessage(playerPattern.matcher(StringParse.getMessage("TpDeny.Denied")).replaceAll(p.getDisplayName()));
    }
}
