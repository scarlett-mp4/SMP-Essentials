package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.regex.Pattern;

public class SetHome extends SmpCommand {
    public SetHome(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player p = (Player) sender;
        final FileConfiguration c = Smp.getInstance().saveConfig.getConfig();
        final Pattern HOME_COUNT = Pattern.compile("[HOME_COUNT]", Pattern.LITERAL);
        int i = 0;
        if (c.contains(p.getUniqueId() + ".homes")) {
            for (final String s : c.getConfigurationSection((p.getUniqueId() + ".homes")).getKeys(false)) {
                if (++i > Smp.getInstance().getConfig().getInt("Homes.Max")) {
                    p.sendMessage(HOME_COUNT.matcher(StringParse.getMessage("SetHome.TooMany")).replaceAll(String.valueOf(Smp.getInstance().getConfig().getInt("Homes.Max"))));
                    return;
                }
            }
        }
        if (args.length == 0) {
            c.set(p.getUniqueId() + ".homes.home", StringParse.parseLocation(p.getLocation()));
        }
        if (args.length > 0) {
            if (c.contains(p.getUniqueId() + ".homes." + args[0])) {
                p.sendMessage(StringParse.getMessage("SetHome.Exists"));
                return;
            }
            c.set(p.getUniqueId() + ".homes." + args[0], StringParse.parseLocation(p.getLocation()));
        }
        p.sendMessage(StringParse.getMessage("SetHome.Created"));
        Smp.getInstance().saveConfig.saveConfig();
    }
}
