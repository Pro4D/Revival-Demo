package com.pro4d.revivaldemo;

import com.pro4d.revivaldemo.command.RevivalReload;
import com.pro4d.revivaldemo.listener.RevivalListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class RevivalDemo extends JavaPlugin {

    private Map<UUID, UUID> mounted;
    private Map<UUID, Integer> logoutMap;

    private List<UUID> gracePeriod;

    private Material revivalItemMaterial = Material.GOLDEN_APPLE;
    private int downedTime = 40;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logoutMap = new HashMap<>();
        mounted = new HashMap<>();

        gracePeriod = new ArrayList<>();

        new RevivalReload(this);
        new RevivalListener(this);

        loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    public void loadConfig() {
        ProConfig proConfig = new ProConfig("config", this);
        FileConfiguration config = proConfig.getConfig();

        config.addDefault("Downed-Time", 60);
        config.addDefault("Revival-Item", Material.GOLDEN_APPLE.name());

        downedTime = config.getInt("Downed-Time");
        String revivalItem = config.getString("Revival-Item");
        assert revivalItem != null;

        Material mat = findMat(revivalItem);
        if(mat != null) {
            revivalItemMaterial = mat;
        } else revivalItemMaterial = Material.GOLDEN_APPLE;

    }

    public Material findMat(String name) {
        for(Material mat : Material.values()) {
            if(mat.name().equalsIgnoreCase(name)) return mat;
        }
        return null;
    }

    public Map<UUID, UUID> getMounted() {
        return mounted;
    }

    public Map<UUID, Integer> getLogoutMap() {
        return logoutMap;
    }

    public List<UUID> getGracePeriod() {return gracePeriod;}

    public Material getRevivalItemMaterial() {
        return revivalItemMaterial;
    }

    public int getDownedTime() {
        return downedTime;
    }

 }
