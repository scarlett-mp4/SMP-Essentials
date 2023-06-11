package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reply extends SmpCommand {
    public Reply(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player p = (Player) sender;
        if (!Smp.getInstance().replyMap.containsKey(p)) {
            p.sendMessage(StringParse.getMessage("Reply.NoReply"));
            return;
        }
        Bukkit.dispatchCommand(p, "msg " + Smp.getInstance().replyMap.get(p).getName() + " " + this.join(args));
    }

    private String join(final String[] args) {
        final StringBuilder sb = new StringBuilder();
        for (final String s : args) {
            sb.append(s).append(" ");
        }
        return sb.toString();
    }
}
