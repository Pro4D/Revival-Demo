package com.pro4d.revivaldemo;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class ProConfig {

    private File file;
    private final FileConfiguration config;
    public ProConfig(String name, RevivalDemo plugin) {
        config = createConfig(name, plugin);
    }

    public FileConfiguration getConfig() {return config;}

//    public void loadConfig() {config = YamlConfiguration.loadConfiguration(file);}

    private FileConfiguration createConfig(String name, RevivalDemo plugin) {
        String configName = name + ".yml";

        String path = plugin.getDataFolder().getAbsolutePath() + "/" + configName;
        plugin.getLogger().log(Level.CONFIG, "P: " + path);

        Path configPath = Paths.get(path);

        if(!configPath.toFile().exists()) {
            try {
                file = Files.createFile(configPath).toFile();
            } catch (IOException e) {
//                plugin.getLogger().log(Level.CONFIG, "Could not find '" + name + ".yml', creating one now...");
                throw new RuntimeException(e);
            }
            plugin.saveResource(configName, true);
        } else {file = configPath.toFile();}

        return YamlConfiguration.loadConfiguration(file);
    }

}
