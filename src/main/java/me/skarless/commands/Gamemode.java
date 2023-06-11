package me.skarless.commands;

import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Gamemode extends SmpCommand {
    public Gamemode(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (this.hasValidArgs(sender, args)) {
            Player target = null;
            try {
                if (!(sender instanceof Player)) {
                    args[1].isBlank();
                } else {
                    target = (Player) sender;
                }
            } catch (Exception e) {
                sender.sendMessage(StringParse.getMessage("Gamemode.IncludeAPlayer"));
                return;
            }
            try {
                if (args.length > 1 && Bukkit.getPlayer(args[1]).isOnline()) {
                    target = Bukkit.getPlayer(args[1]);
                }
            } catch (Exception e) {
                sender.sendMessage(StringParse.getMessage("General.PlayerNotOnline"));
                return;
            }
            this.setGamemode(target, this.getGamemode(args[0]), sender);
        }
    }

    private GameMode getGamemode(String mode) {
        final String upperCase;
        mode = (upperCase = mode.toUpperCase());
        switch (upperCase) {
            case "1":
            case "C":
            case "CREATIVE": {
                return GameMode.CREATIVE;
            }
            case "0":
            case "S":
            case "SURVIVAL": {
                return GameMode.SURVIVAL;
            }
            case "3":
            case "SPECTATOR": {
                return GameMode.SPECTATOR;
            }
            case "2":
            case "A":
            case "ADVENTURE": {
                return GameMode.ADVENTURE;
            }
            default: {
                return null;
            }
        }
    }

    private boolean hasValidArgs(final CommandSender sender, final String[] args) {
        try {
            args[0].isBlank();
        } catch (Exception e) {
            sender.sendMessage(StringParse.getMessage("Gamemode.IncludeAGamemode"));
            return false;
        }
        if (this.getGamemode(args[0]) == null) {
            sender.sendMessage(StringParse.getMessage("Gamemode.UnknownGamemode"));
            return false;
        }
        return true;
    }

    private void setGamemode(final Player p, final GameMode mode, final CommandSender sender) {
        final Pattern modePattern = Pattern.compile("[MODE]", 16);
        final Pattern playerPattern = Pattern.compile("[PLAYER]", 16);
        if (p != sender && !sender.hasPermission("smp.gamemode.others")) {
            sender.sendMessage(StringParse.getMessage("Gamemode.CantSetPlayerMode"));
            return;
        }
        final GameMode[] values = GameMode.values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final GameMode option = values[i];
            if (option == mode) {
                if (!sender.hasPermission("smp.gamemode.creative")) {
                    sender.sendMessage(modePattern.matcher(StringParse.getMessage("")).replaceAll(mode.toString()));
                    return;
                }
                p.setGameMode(mode);
                p.sendMessage(modePattern.matcher(StringParse.getMessage("Gamemode.Set")).replaceAll(mode.toString().toLowerCase()));
                if (sender != p) {
                    sender.sendMessage(playerPattern.matcher(modePattern.matcher(StringParse.getMessage("Gamemode.SetPlayer")).replaceAll(mode.toString().toLowerCase())).replaceAll(p.getDisplayName()));
                    break;
                }
                break;
            } else {
                ++i;
            }
        }
    }
}
