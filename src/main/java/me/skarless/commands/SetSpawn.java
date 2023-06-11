package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetSpawn extends SmpCommand {
    public SetSpawn(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player p = (Player) sender;
        Smp.getInstance().saveConfig.getConfig().set("Spawn.X", p.getLocation().getX());
        Smp.getInstance().saveConfig.getConfig().set("Spawn.Y", p.getLocation().getY());
        Smp.getInstance().saveConfig.getConfig().set("Spawn.Z", p.getLocation().getZ());
        Smp.getInstance().saveConfig.getConfig().set("Spawn.Pitch", p.getLocation().getPitch());
        Smp.getInstance().saveConfig.getConfig().set("Spawn.Yaw", p.getLocation().getYaw());
        Smp.getInstance().saveConfig.getConfig().set("Spawn.World", Objects.requireNonNull(p.getLocation().getWorld()).getName());
        Smp.getInstance().saveConfig.saveConfig();
        p.sendMessage(StringParse.getMessage("SetSpawn.Set"));
    }
}
