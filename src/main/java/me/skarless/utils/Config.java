package me.skarless.utils;

import me.skarless.Smp;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Config {
    private final String path;
    private FileConfiguration config;
    private File file;

    public Config(final String path) {
        this.path = path;
        this.config = null;
        this.file = null;
    }

    public void reloadConfig() {
        if (this.file == null) {
            this.file = new File(Smp.getInstance().getDataFolder(), this.path);
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
        final InputStream doorStream = Smp.getInstance().getResource(this.path);
        if (doorStream != null) {
            final YamlConfiguration defaultWarpConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(doorStream));
            this.config.setDefaults(defaultWarpConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }
        return this.config;
    }

    public void saveConfig() {
        if (this.config == null || this.file == null) {
            return;
        }
        try {
            this.getConfig().save(this.file);
        } catch (IOException e) {
            Smp.getInstance().getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    public void saveDefaultConfig() {
        if (this.file == null) {
            this.file = new File(Smp.getInstance().getDataFolder(), this.path);
        }
        if (!this.file.exists()) {
            Smp.getInstance().saveResource(this.path, false);
        }
    }
}
