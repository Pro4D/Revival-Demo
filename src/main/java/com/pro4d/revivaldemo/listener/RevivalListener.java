package com.pro4d.revivaldemo.listener;

import com.pro4d.revivaldemo.RevivalDemo;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.List;

public class RevivalListener implements Listener {

    private final RevivalDemo plugin;
    private final List<EntityDamageEvent.DamageCause> dontDown;
    public RevivalListener(RevivalDemo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        dontDown = new ArrayList<>();
        dontDown.add(EntityDamageEvent.DamageCause.VOID);
        dontDown.add(EntityDamageEvent.DamageCause.SUICIDE);
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if(event.getEntity().getType() != EntityType.PLAYER) return;
        if(plugin.getGracePeriod().contains(event.getEntity().getUniqueId())) return;

        if(dontDown.contains(event.getCause())) return;
        Player player = (Player) event.getEntity();

        if((player.getHealth() - event.getFinalDamage()) > 0.0) return;
        event.setCancelled(true);

        downPlayer(player, plugin.getDownedTime(), false);
    }

    @EventHandler
    private void onDismount(EntityDismountEvent event) {
        if(event.getDismounted().getType() != EntityType.PIG) return;
        if(!(event.getEntity() instanceof Player)) return;
        if(!plugin.getMounted().containsKey(event.getEntity().getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    private void onInteract(PlayerInteractAtEntityEvent event) {
        if(event.getHand() != EquipmentSlot.HAND) return;
        if(event.getRightClicked().getType() != EntityType.PLAYER) return;
        if(!plugin.getMounted().containsKey(event.getRightClicked().getUniqueId())) return;

        Player player = event.getPlayer();
        if(player.getInventory().getItemInMainHand().getType() != plugin.getRevivalItemMaterial()) return;

        Player clicked = (Player) event.getRightClicked();

        Entity pig = Bukkit.getEntity(plugin.getMounted().get(clicked.getUniqueId()));

        //clicked.getWorld().playSound(clicked.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.MASTER, 5, 1);
        clicked.getWorld().playSound(clicked.getLocation(), Sound.ITEM_TOTEM_USE, SoundCategory.MASTER, 5, 1);

        plugin.getMounted().remove(clicked.getUniqueId());
        clicked.setGameMode(GameMode.SURVIVAL);

        if(pig == null) return;
        pig.removePassenger(clicked);
        pig.remove();
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        if(!plugin.getMounted().containsKey(event.getPlayer().getUniqueId())) return;
        Player player = event.getPlayer();
        downPlayer(player, plugin.getLogoutMap().get(player.getUniqueId()), true);
    }

    @EventHandler
    private void onRespawn(PlayerRespawnEvent event) {
        if(!plugin.getGracePeriod().contains(event.getPlayer().getUniqueId())) return;
        gracePeriodTimer(event.getPlayer());
    }

    private void downPlayer(Player player, int t, boolean logout) {
        World world = player.getWorld();

        Pig pig = (Pig) world.spawnEntity(player.getLocation().subtract(0, .9, 0), EntityType.PIG);

        pig.setGravity(false);
        pig.setAI(false);
        pig.setSaddle(true);
        pig.setFireTicks(0);
        pig.setInvulnerable(true);
        pig.setVisualFire(false);
        pig.addPassenger(player);
        pig.setInvisible(true);
        pig.setSilent(true);

        player.setHealth(.5);
        player.setInvulnerable(true);
        player.setFireTicks(0);
        player.setGameMode(GameMode.ADVENTURE);

        plugin.getMounted().put(player.getUniqueId(), pig.getUniqueId());

        new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                if(!player.isOnline()) cancel();

                player.setFireTicks(0);

                if(!plugin.getLogoutMap().containsKey(player.getUniqueId())) {
                    plugin.getLogoutMap().put(player.getUniqueId(), time++);
                } else {
                    plugin.getLogoutMap().replace(player.getUniqueId(), time++);
                }

                if(!logout) {
                    if(time == plugin.getDownedTime()) {
                        plugin.getGracePeriod().add(player.getUniqueId());

                        player.setInvulnerable(false);
                        Entity pig = Bukkit.getEntity(plugin.getMounted().get(player.getUniqueId()));
                        plugin.getMounted().remove(player.getUniqueId());

                        player.damage(20);

                        if(pig != null) pig.remove();
                        cancel();
                    }

                } else {
                    if(time == t) {
                        plugin.getGracePeriod().add(player.getUniqueId());

                        player.setInvulnerable(false);
                        Entity pig = Bukkit.getEntity(plugin.getMounted().get(player.getUniqueId()));

                        plugin.getMounted().remove(player.getUniqueId());
                        player.damage(20);

                        if(pig != null) pig.remove();
                        cancel();
                    }
                }

            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void gracePeriodTimer(Player player) {
        if(!plugin.getGracePeriod().contains(player.getUniqueId())) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getGracePeriod().remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, 60);
    }

//    @EventHandler
//    private void onMove(PlayerMoveEvent event) {
//        if(!plugin.getMountedPigs().containsKey(event.getPlayer().getUniqueId())) return;
//        event.setCancelled(true);
//    }

}
