package me.skarless.commands;

import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Workbench extends SmpCommand {
    public Workbench(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player p = (Player) sender;
        p.openWorkbench(null, true);
        p.sendMessage(StringParse.getMessage("Workbench.Open"));
    }
}
