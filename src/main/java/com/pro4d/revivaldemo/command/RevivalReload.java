package com.pro4d.revivaldemo.command;

import com.pro4d.revivaldemo.RevivalDemo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

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
        if(sender instanceof Player) sender.sendMessage(ChatColor.GREEN + "Plugin config reloaded!");
        return false;
    }
}
