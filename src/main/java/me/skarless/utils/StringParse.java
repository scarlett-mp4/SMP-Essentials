package me.skarless.utils;

import me.skarless.Smp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Objects;

public class StringParse {
    public static String getMessage(final String path) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Smp.getInstance().messageConfig.getConfig().getString(path)));
    }

    public static String parseLocation(final Location loc) {
        return (loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch());
    }

    public static Location parseLocation(final String s) {
        final String[] args = s.split(" ");
        return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
    }
}
