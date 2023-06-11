package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Nick extends SmpCommand {
    public Nick(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Pattern NICK = Pattern.compile("[NICK]", Pattern.LITERAL);
        final Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(StringParse.getMessage("Nick.Reset"));
            p.setDisplayName(p.getName());
            p.setPlayerListName(p.getName());
            Smp.getInstance().saveConfig.getConfig().set(p.getUniqueId() + ".nick", p.getName());
        } else {
            final String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
            p.setDisplayName(nick);
            p.setPlayerListName(nick);
            p.sendMessage(NICK.matcher(StringParse.getMessage("Nick.Changed")).replaceAll(nick));
            Smp.getInstance().saveConfig.getConfig().set(p.getUniqueId() + ".nick", nick);
        }
        Smp.getInstance().saveConfig.saveConfig();
    }
}
