package com.pro4d.revivaldemo;

import com.pro4d.revivaldemo.command.RevivalReload;
import com.pro4d.revivaldemo.listener.RevivalListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class RevivalDemo extends JavaPlugin {

    private List<UUID> armorStands;
    private List<UUID> downed;
    private Map<UUID, Integer> logoutMap;
    private Material reviveItem = Material.GOLDEN_APPLE;
    private int downedTime = 60;

    //private ProConfig config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logoutMap = new HashMap<>();
        armorStands = new ArrayList<>();
        downed = new ArrayList<>();

        new RevivalReload(this);
        new RevivalListener(this);

        loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(UUID u : armorStands) {
            Entity e = Bukkit.getEntity(u);
            if(e == null) continue;
            e.remove();
        }

    }

    public void loadConfig() {

    }

}
