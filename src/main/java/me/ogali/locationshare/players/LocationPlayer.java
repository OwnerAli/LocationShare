package me.ogali.locationshare.players;

import me.ogali.locationshare.LocationSharePlugin;
import me.ogali.locationshare.cooldowns.Cooldown;
import me.ogali.locationshare.util.Chat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Optional;

public class LocationPlayer {

    private final Player player;
    private final Cooldown announceCommandCooldown;
    private final Cooldown messageCommandCooldown;
    private final Cooldown placeholderCooldown;

    private String finalLocationMessage;

    public LocationPlayer(Player player) {
        this.player = player;
        this.announceCommandCooldown = new Cooldown(5);
        this.messageCommandCooldown = new Cooldown(5);
        this.placeholderCooldown = new Cooldown(5);
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<String> getPlaceholderMessageAndAddCooldown() {
        if (placeholderCooldown.isActive()) return Optional.empty();

        placeholderCooldown.start();
        return Optional.ofNullable(getPlayerLocationMessage());
    }

    public void announcePlayerLocation() {
        if (announceCommandCooldown.isActive()) {
            Chat.tell(player, "&cYou're on cooldown for another: " +
                    announceCommandCooldown.getRemainingTime() + " seconds.");
            return;
        }
        getClickableLocationMessage().ifPresent(clickableLocationMessage -> Bukkit.getOnlinePlayers()
                .forEach(onlinePlayer -> player.spigot().sendMessage(clickableLocationMessage)));
        announceCommandCooldown.start();
    }

    public void messagePlayerLocation(Player recipient) {
        if (messageCommandCooldown.isActive()) {
            Chat.tell(player, "&cYou're on cooldown for another: " +
                    messageCommandCooldown.getRemainingTime() + " seconds.");
            return;
        }
        getClickableLocationMessage().ifPresent(clickableLocationMessage ->
                recipient.spigot().sendMessage(clickableLocationMessage));
        Chat.tell(player, "&aYour location was sent to " + recipient.getName());
        messageCommandCooldown.start();
    }

    private Optional<TextComponent> getClickableLocationMessage() {
        FileConfiguration config = LocationSharePlugin.getInstance().getConfig();
        String configAnnounceMessage = Chat.colorize(config.getString("General.Tp-Message"));
        String configClickCommand = config.getString("General.Tp-Command");
        String configHoverMessage = Chat.colorize(config.getString("General.Tp-Hover-Message"));

        if (configClickCommand == null) return Optional.empty();

        String modifiedAnnounceMessage = configAnnounceMessage
                .replace("@location", getPlayerLocationMessage())
                .replace("@name", player.getName())
                .replace("@display", Chat.colorize(player.getDisplayName()));
        String modifiedClickCommand = configClickCommand.replace("@player", player.getName());

        TextComponent clickableLocationMessage = new TextComponent(modifiedAnnounceMessage);

        clickableLocationMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, modifiedClickCommand));
        clickableLocationMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(configHoverMessage)));

        return Optional.of(clickableLocationMessage);
    }

    private String getPlayerLocationMessage() {
        DecimalFormat decimalFormat = new DecimalFormat("###.###");

        Optional<String> locationMessageSection = Optional.ofNullable(LocationSharePlugin.getInstance()
                .getConfig().getString("General.Placeholder-Message"));

        locationMessageSection.ifPresentOrElse(message -> {
            String formattedPlayerXCoordinate = decimalFormat.format(player.getLocation().getX());
            String formattedPlayerYCoordinate = decimalFormat.format(player.getLocation().getY());
            String formattedPlayerZCoordinate = decimalFormat.format(player.getLocation().getZ());
            String formattedPlayerLocationMessage = formattedPlayerXCoordinate + ", " + formattedPlayerYCoordinate + ", " + formattedPlayerZCoordinate;

            finalLocationMessage = message.replace("@player", player.getName())
                    .replace("@location", formattedPlayerLocationMessage);
            finalLocationMessage = Chat.colorize(finalLocationMessage);
        }, () -> player.sendMessage("An error occurred while processing your request."));
        return finalLocationMessage;
    }

}
