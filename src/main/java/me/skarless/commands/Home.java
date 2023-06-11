package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Home extends SmpCommand {
    public Home(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final FileConfiguration c = Smp.getInstance().saveConfig.getConfig();
        final Player p = (Player) sender;
        if (c.contains(String.valueOf(p.getUniqueId()))) {
            final List<String> homes = new ArrayList<String>(c.getConfigurationSection(String.valueOf(p.getUniqueId())).getKeys(false));
            if (homes.size() == 0) {
                p.sendMessage(StringParse.getMessage("Home.NoHomes"));
                return;
            }
            for (final String s : homes) {
                if (args.length == 0) {
                    if (c.contains(String.valueOf(p.getUniqueId()))) {
                        this.teleport(p, "home", c);
                    } else {
                        p.sendMessage(StringParse.getMessage("Home.Choose"));
                    }
                    return;
                }
                if (s.equalsIgnoreCase(args[0])) {
                    this.teleport(p, s, c);
                    return;
                }
            }
            p.sendMessage(StringParse.getMessage("Home.Choose"));
        } else {
            p.sendMessage(StringParse.getMessage("Home.NoHomes"));
        }
    }

    private void teleport(final Player p, final String s, final FileConfiguration c) {
        final Pattern TIME = Pattern.compile("[TIME]", 16);
        final int x = (int) Math.round(p.getLocation().getX());
        final int y = (int) Math.round(p.getLocation().getY());
        final int z = (int) Math.round(p.getLocation().getZ());
        p.sendMessage(TIME.matcher(StringParse.getMessage("Home.Teleporting")).replaceAll(String.valueOf(Smp.getInstance().getConfig().getInt("Homes.WaitTime"))));
        final int n = 0;
        final int n2 = 0;
        final int n3 = 0;
        Bukkit.getScheduler().runTaskLater(Smp.getInstance(), () -> {
            if (n == (int) Math.round(p.getLocation().getX()) && n2 == (int) Math.round(p.getLocation().getY()) && n3 == (int) Math.round(p.getLocation().getZ())) {
                p.teleport(StringParse.parseLocation(c.getString(String.valueOf(p.getUniqueId()), s)));
                p.sendMessage(StringParse.getMessage("Home.Warped"));
            } else {
                p.sendMessage(StringParse.getMessage("Home.PlayerMoved"));
            }
        }, 20L * Smp.getInstance().getConfig().getLong("Homes.WaitTime"));
    }
}
