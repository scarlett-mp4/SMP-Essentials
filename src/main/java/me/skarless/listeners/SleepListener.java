package me.skarless.listeners;

import me.skarless.Smp;
import me.skarless.utils.StringParse;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class SleepListener implements Listener {
    final Pattern PLAYER;
    final Pattern SLEEPING;
    final Pattern NEEDED;
    private final List<Player> sleeping;
    int onlineInOverworld;
    int needed;
    private boolean ongoing;

    public SleepListener() {
        this.PLAYER = Pattern.compile("[PLAYER]", 16);
        this.SLEEPING = Pattern.compile("[SLEEPING]", 16);
        this.NEEDED = Pattern.compile("[NEEDED]", 16);
        this.sleeping = new ArrayList<Player>();
        this.onlineInOverworld = 0;
        this.needed = 1;
        this.ongoing = false;
    }

    private void update() {
        this.onlineInOverworld = 0;
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().getWorld().getName().equals(Smp.getInstance().getConfig().getString("Sleep.World"))) {
                ++this.onlineInOverworld;
            }
        }
        this.needed = (int) Math.round(this.onlineInOverworld * (Smp.getInstance().getConfig().getDouble("Sleep.PlayerSleepPercentage") * 0.01));
    }

    private void check() {
        if (this.ongoing) {
            return;
        }
        if (this.needed == 0) {
            return;
        }
        if (this.sleeping.size() == 0) {
            return;
        }
        if (this.sleeping.size() >= (double) this.needed) {
            this.sleeping.clear();
            Bukkit.getScheduler().runTaskLater(Smp.getInstance(), () -> {
                int i = 0;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(StringParse.getMessage("Sleep.Accelerating"));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(StringParse.getMessage("Sleep.Accelerating")));
                }
            }, 1L);
            final World world = Bukkit.getWorld(Smp.getInstance().getConfig().getString("Sleep.World"));
            final int addTime = (int) (24000L - world.getTime()) / 96;
            final AtomicInteger i = new AtomicInteger(0);
            this.ongoing = true;
            new BukkitRunnable() {
                public void run() {
                    world.setTime(world.getFullTime() + addTime);
                    i.getAndIncrement();
                    if (i.get() >= 96) {
                        SleepListener.this.ongoing = false;
                        this.cancel();
                    }
                }
            }.runTaskTimer(Smp.getInstance(), 0L, 1L);
        }
    }

    @EventHandler
    public void onPlayerSleep(final PlayerBedEnterEvent e) {
        if (!Smp.getInstance().getConfig().getBoolean("Sleep.SkipNight")) {
            return;
        }
        if (this.ongoing) {
            return;
        }
        if (!e.getBedEnterResult().name().equals("OK")) {
            return;
        }
        final Player p = e.getPlayer();
        this.update();
        this.sleeping.add(p);
        String message = this.PLAYER.matcher(StringParse.getMessage("Sleep.Slept")).replaceAll(p.getDisplayName());
        message = this.SLEEPING.matcher(message).replaceAll(String.valueOf(this.sleeping.size()));
        message = this.NEEDED.matcher(message).replaceAll(String.valueOf(this.needed));
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
        final String finalMessage = message;
        Bukkit.getScheduler().runTaskLater(Smp.getInstance(), () -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(finalMessage)), 1L);
        this.check();
    }

    @EventHandler
    public void onPlayerWake(final PlayerBedLeaveEvent e) {
        if (!Smp.getInstance().getConfig().getBoolean("Sleep.SkipNight")) {
            return;
        }
        if (this.ongoing) {
            return;
        }
        final Player p = e.getPlayer();
        this.update();
        this.sleeping.remove(p);
        String message = this.PLAYER.matcher(StringParse.getMessage("Sleep.Awake")).replaceAll(p.getDisplayName());
        message = this.SLEEPING.matcher(message).replaceAll(String.valueOf(this.sleeping.size()));
        message = this.NEEDED.matcher(message).replaceAll(String.valueOf(this.needed));
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
        this.check();
    }

    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskLater(Smp.getInstance(), this::update, 2L);
        Bukkit.getScheduler().runTaskLater(Smp.getInstance(), this::check, 4L);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskLater(Smp.getInstance(), this::update, 2L);
        Bukkit.getScheduler().runTaskLater(Smp.getInstance(), this::check, 4L);
    }
}
