package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class DelHome extends SmpCommand {
    public DelHome(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player p = (Player) sender;
        final FileConfiguration c = Smp.getInstance().saveConfig.getConfig();
        if (args.length == 0) {
            p.sendMessage(StringParse.getMessage("DelHome.Choose"));
            return;
        }
        if (c.contains(p.getUniqueId() + ".homes")) {
            for (final String s : c.getConfigurationSection(p.getUniqueId() + ".homes").getKeys(false)) {
                if (Objects.equals(s.toLowerCase(), args[0].toLowerCase())) {
                    c.set(p.getUniqueId() + ".homes." + s, null);
                    Smp.getInstance().saveConfig.saveConfig();
                    p.sendMessage(StringParse.getMessage("DelHome.Deleted"));
                    return;
                }
            }
            p.sendMessage(StringParse.getMessage("DelHome.Invalid"));
        } else {
            p.sendMessage(StringParse.getMessage("DelHome.NoHomes"));
        }
    }
}
