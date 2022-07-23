package com.pro4d.revivaldemo;

import com.pro4d.revivaldemo.command.RevivalReload;
import com.pro4d.revivaldemo.listener.RevivalListener;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class RevivalDemo extends JavaPlugin {

    private Map<UUID, UUID> mounted;
    private Map<UUID, Integer> logoutMap;

    private List<UUID> gracePeriod;

    private Material reviveItem = Material.GOLDEN_APPLE;
    private int downedTime = 40;

    //private ProConfig config;

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

    }

    public Map<UUID, UUID> getMounted() {
        return mounted;
    }

    public Map<UUID, Integer> getLogoutMap() {
        return logoutMap;
    }

    public List<UUID> getGracePeriod() {return gracePeriod;}

    public Material getReviveItem() {
        return reviveItem;
    }

    public int getDownedTime() {
        return downedTime;
    }

    public void setReviveItem(Material reviveItem) {
        this.reviveItem = reviveItem;
    }

    public void setDownedTime(int downedTime) {
        this.downedTime = downedTime;
    }
}
