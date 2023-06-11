package me.skarless;

import me.skarless.commands.*;
import me.skarless.listeners.ChatListener;
import me.skarless.listeners.LoginListener;
import me.skarless.listeners.RespawnListener;
import me.skarless.listeners.SleepListener;
import me.skarless.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class Smp extends JavaPlugin {
    private static Smp instance;
    public ArrayList<String> requestList;
    public HashMap<Player, Player> replyMap;
    public Config saveConfig;
    public Config messageConfig;

    public Smp() {
        this.saveConfig = new Config("save.yml");
        this.messageConfig = new Config("messages.yml");
    }

    public static Smp getInstance() {
        return Smp.instance;
    }

    public void onEnable() {
        Smp.instance = this;
        this.requestList = new ArrayList<>();
        this.replyMap = new HashMap<>();
        this.initConfiguration();
        this.initCommands();
        this.initListeners();
        Bukkit.getConsoleSender().sendMessage("§x§9§f§f§f§9§6[SMP Essentials] has been enabled!");
    }

    public void initListeners() {
        Bukkit.getPluginManager().registerEvents(new LoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new SleepListener(), this);
    }

    public void initCommands() {
        new SetSpawn("setspawn", "smp.setspawn", false);
        new Broadcast("broadcast", "smp.broadcast", true);
        new Enderchest("enderchest", "smp.enderchest", false);
        new Gamemode("gamemode", "smp.gamemode", true);
        new Msg("msg", "smp.msg", true);
        new Spawn("spawn", "smp.spawn", true);
        new Tpa("tpa", "smp.tpa", false);
        new Tpaccept("tpaccept", "smp.tpaccept", false);
        new Tpdeny("tpdeny", "smp.tpdeny", false);
        new Workbench("workbench", "smp.workbench", false);
        new Nick("nick", "smp.nick", false);
        new Reply("reply", "smp.reply", false);
        new DelHome("delhome", "smp.delhome", false);
        new SetHome("sethome", "smp.sethome", false);
        new Home("home", "smp.home", false);
        new Homes("homes", "smp.homes", false);
    }

    public void initConfiguration() {
        try {
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdirs();
            }
            this.saveDefaultConfig();
            this.saveConfig.saveDefaultConfig();
            this.messageConfig.saveDefaultConfig();
        } catch (Exception e) {
            e.printStackTrace();
            this.getLogger().severe("[SMP Essentials] Error loading configuration files!");
        }
    }
}
