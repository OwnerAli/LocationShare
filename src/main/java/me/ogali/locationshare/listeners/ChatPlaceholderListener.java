package me.ogali.locationshare.listeners;

import me.ogali.locationshare.LocationSharePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatPlaceholderListener implements Listener {

    private final LocationSharePlugin main;

    public ChatPlaceholderListener(LocationSharePlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.getMessage().contains("[Location]")) return;
        main.getLocationPlayerRegistry().getLocationPlayer(event.getPlayer())
                .ifPresentOrElse(locationPlayer -> locationPlayer.getPlaceholderMessageAndAddCooldown()
                                .ifPresent(event::setMessage),
                        () -> event.getPlayer().sendMessage("An error occurred while processing your request."));
    }

}
