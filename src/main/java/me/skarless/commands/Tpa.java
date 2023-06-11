package me.skarless.commands;

import me.skarless.Smp;
import me.skarless.utils.SmpCommand;
import me.skarless.utils.StringParse;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Tpa extends SmpCommand {
    public Tpa(final String commandName, final String permission, final boolean canConsoleExecute) {
        super(commandName, permission, canConsoleExecute);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Pattern playerPattern = Pattern.compile("[PLAYER]", 16);
        final Pattern timeoutPattern = Pattern.compile("[TIMEOUT]", 16);
        final Pattern acceptPattern = Pattern.compile("[ACCEPT]", 16);
        final Pattern declinePattern = Pattern.compile("[DECLINE]", 16);
        final Pattern linePattern = Pattern.compile("[LINE]", 16);
        final Player p = (Player) sender;
        Player target = null;
        if (args.length == 0) {
            p.sendMessage(StringParse.getMessage("Tpa.IncludePlayer"));
            return;
        }
        try {
            if (Bukkit.getPlayer(args[0]).isOnline()) {
                target = Bukkit.getPlayer(args[0]);
            }
        } catch (Exception e) {
            p.sendMessage(StringParse.getMessage("General.PlayerNotOnline"));
            return;
        }
        assert target != null;
        if (Smp.getInstance().requestList.contains(p.getUniqueId() + " " + target.getUniqueId())) {
            p.sendMessage(StringParse.getMessage("Tpa.AlreadySentToPlayer"));
            return;
        }
        if (p == target) {
            p.sendMessage(StringParse.getMessage("Tpa.CantTeleportToSelf"));
            return;
        }
        p.sendMessage(timeoutPattern.matcher(playerPattern.matcher(StringParse.getMessage("Tpa.TpaSent")).replaceAll(target.getDisplayName())).replaceAll(String.valueOf(Smp.getInstance().getConfig().getInt("Tpa.Timeout"))));
        Smp.getInstance().requestList.add(p.getUniqueId() + " " + target.getUniqueId());
        final Player finalTarget = target;
        Bukkit.getScheduler().runTaskLater(Smp.getInstance(), () -> {
            if (Smp.getInstance().requestList.contains(p.getUniqueId() + " " + finalTarget.getUniqueId())) {
                Smp.getInstance().requestList.remove(p.getUniqueId() + " " + finalTarget.getUniqueId());
                p.sendMessage(timeoutPattern.matcher(StringParse.getMessage("Tpa.TimedOut")).replaceAll(finalTarget.getDisplayName()));
            }
        }, 20L * Smp.getInstance().getConfig().getInt("Tpa.Timeout"));
        final String raw = StringParse.getMessage("Tpa.RequestReceived");
        final ComponentBuilder builder = new ComponentBuilder();
        final TextComponent acceptButton = new TextComponent(StringParse.getMessage("Tpa.Accept"));
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, p.getName()));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringParse.getMessage("Tpa.AcceptMessage")).create()));
        final TextComponent declineButton = new TextComponent(StringParse.getMessage("Tpa.Decline"));
        declineButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, p.getName()));
        declineButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringParse.getMessage("Tpa.DeclineMessage")).create()));
        final StringBuilder sb = new StringBuilder();
        for (final String word : raw.split("\\s+")) {
            if (acceptPattern.matcher(word).find()) {
                builder.append(sb.toString());
                sb.setLength(0);
                builder.append(acceptButton).append(" ");
            } else if (linePattern.matcher(word).find()) {
                builder.append(sb.toString());
                sb.setLength(0);
                builder.append(linePattern.matcher(word).replaceAll("\n")).append(" ");
            } else if (declinePattern.matcher(word).find()) {
                builder.append(sb.toString());
                sb.setLength(0);
                builder.append(declineButton).append(" ");
            } else if (playerPattern.matcher(word).find()) {
                builder.append(sb.toString());
                sb.setLength(0);
                builder.append(p.getDisplayName()).append(" ");
            } else {
                sb.append(word).append(" ");
            }
        }
        target.spigot().sendMessage(builder.create());
    }
}
