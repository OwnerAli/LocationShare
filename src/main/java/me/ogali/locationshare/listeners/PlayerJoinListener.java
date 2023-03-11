package me.ogali.locationshare.listeners;

import me.ogali.locationshare.LocationSharePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final LocationSharePlugin main;

    public PlayerJoinListener(LocationSharePlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("ls.clickable") &&
                !event.getPlayer().hasPermission("ls.placeholder")) return;
        main.getLocationPlayerRegistry().registerLocationPlayer(event.getPlayer());
    }

}
