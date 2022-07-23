package com.pro4d.revivaldemo;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ProConfig {

    private final String name;
    private File file;
    private FileConfiguration config;
    public ProConfig(String name) {
        this.name = name;
    }

}
