package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Homes extends SmpCommand {
    public Homes(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Pattern HOMES_FORMATTED = Pattern.compile("[HOMES_FORMATTED]", 16);
        final FileConfiguration c = Smp.getInstance().saveConfig.getConfig();
        final Player p = (Player) sender;
        if (c.contains(String.valueOf(p.getUniqueId()))) {
            final List<String> homes = new ArrayList<String>(Objects.requireNonNull(c.getConfigurationSection(String.valueOf(p.getUniqueId()))).getKeys(false));
            final StringBuilder sb = new StringBuilder();
            for (final String s : homes) {
                sb.append(s).append(", ");
            }
            final String message = HOMES_FORMATTED.matcher(StringParse.getMessage("Homes.List")).replaceAll(sb.toString());
            if (homes.size() == 0) {
                p.sendMessage(StringParse.getMessage("Homes.NoHomes"));
            } else {
                p.sendMessage(message.substring(0, message.length() - 2));
            }
        } else {
            p.sendMessage(StringParse.getMessage("Homes.NoHomes"));
        }
    }
}
