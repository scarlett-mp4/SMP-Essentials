package me.skarless.commands;

import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Enderchest extends SmpCommand {
    public Enderchest(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Player p = (Player) sender;
        p.openInventory(p.getEnderChest());
        p.sendMessage(StringParse.getMessage("EnderChest.Open"));
    }
}
