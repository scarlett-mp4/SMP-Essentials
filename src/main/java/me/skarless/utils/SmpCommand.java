package me.skarless.utils;

import me.skarless.Smp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class SmpCommand implements CommandExecutor {
    private final String permission;
    private final boolean canConsoleExecute;

    public SmpCommand(final String commandName, final String permission, final boolean canConsoleExecute) {
        this.permission = permission;
        this.canConsoleExecute = canConsoleExecute;
        Objects.requireNonNull(Smp.getInstance().getCommand(commandName)).setExecutor(this);
    }

    public abstract void execute(final CommandSender p0, final String[] p1);

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        try {
            if (!this.canConsoleExecute && !(sender instanceof Player)) {
                sender.sendMessage(StringParse.getMessage("General.ConsoleBlocked"));
                return true;
            }
            if (this.permission != null) {
                if (sender.hasPermission(this.permission)) {
                    this.execute(sender, args);
                } else {
                    sender.sendMessage(StringParse.getMessage("General.InvalidPermission"));
                }
                return true;
            }
            this.execute(sender, args);
        } catch (Exception e) {
            sender.sendMessage(StringParse.getMessage("General.Error"));
            e.printStackTrace();
        }
        return true;
    }
}
