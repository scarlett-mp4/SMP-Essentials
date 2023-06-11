package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Spawn extends SmpCommand {
    public Spawn(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player p = (Player) sender;
        final FileConfiguration c = Smp.getInstance().saveConfig.getConfig();
        try {
            final Location loc = new Location(Bukkit.getWorld(c.getString("Spawn.World")), c.getDouble("Spawn.X"), c.getDouble("Spawn.Y"), c.getDouble("Spawn.Z"), (float) c.getDouble("Spawn.Yaw"), (float) c.getDouble("Spawn.Pitch"));
            p.teleport(loc);
            p.sendMessage(StringParse.getMessage("Spawn.Warped"));
        } catch (Exception e) {
            p.sendMessage(StringParse.getMessage("Spawn.NoSpawn"));
        }
    }
}
