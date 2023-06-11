package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Msg extends SmpCommand {
    public Msg(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Pattern fromPattern = Pattern.compile("[FROM]", 16);
        final Pattern toPattern = Pattern.compile("[TO]", 16);
        final Pattern messagePattern = Pattern.compile("[MESSAGE]", 16);
        Player target = null;
        String senderName = "Console";
        if (args.length == 0) {
            sender.sendMessage(StringParse.getMessage("Message.IncludePlayer"));
            return;
        }
        try {
            if (Bukkit.getPlayer(args[0]).isOnline()) {
                target = Bukkit.getPlayer(args[0]);
            }
        } catch (Exception e) {
            sender.sendMessage(StringParse.getMessage("General.PlayerNotOnline"));
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(StringParse.getMessage("Message.IncludeMessage"));
            return;
        }
        if (sender instanceof Player) {
            senderName = ((Player) sender).getDisplayName();
            Smp.getInstance().replyMap.put((Player) sender, target);
            Smp.getInstance().replyMap.put(target, (Player) sender);
        }
        String message = fromPattern.matcher(StringParse.getMessage("Message.Format")).replaceAll(senderName);
        message = toPattern.matcher(message).replaceAll(target.getDisplayName());
        message = messagePattern.matcher(message).replaceAll(this.join(args));
        sender.sendMessage(message);
        target.sendMessage(message);
    }

    private String join(final String[] args) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (final String s : args) {
            if (i != 0) {
                sb.append(s).append(" ");
            }
            ++i;
        }
        return sb.toString();
    }
}
