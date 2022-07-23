package com.pro4d.revivaldemo.command;

import com.pro4d.revivaldemo.RevivalDemo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class RevivalReload implements CommandExecutor {

    private final RevivalDemo plugin;
    public RevivalReload(RevivalDemo plugin) {
        this.plugin = plugin;
        PluginCommand cmd = plugin.getServer().getPluginCommand("revivereload");
        if(cmd == null) return;
        cmd.setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.loadConfig();

        //send message to confirm reload
        return false;
    }
}
