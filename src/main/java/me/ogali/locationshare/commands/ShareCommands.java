package me.ogali.locationshare.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import me.ogali.locationshare.LocationSharePlugin;
import me.ogali.locationshare.players.LocationPlayer;
import me.ogali.locationshare.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("locationshare|ls")
public class ShareCommands extends BaseCommand {

    private final LocationSharePlugin main;

    public ShareCommands(LocationSharePlugin main) {
        this.main = main;
    }

    @Subcommand("announce")
    @CommandPermission("ls.clickable.announce")
    public void onAnnounce(Player player) {
        main.getLocationPlayerRegistry().getLocationPlayer(player)
                .ifPresent(LocationPlayer::announcePlayerLocation);
    }

    @Subcommand("message")
    @CommandPermission("ls.clickable.message")
    @Syntax("<player-name>")
    public void onMessage(Player player, String recipientPlayerName) {
        main.getLocationPlayerRegistry().getLocationPlayer(player)
                .ifPresent(locationPlayer -> {
                    Player recipient = Bukkit.getPlayer(recipientPlayerName);
                    if (recipient == null) {
                        Chat.tell(player, "&cInvalid or offline player");
                        return;
                    }
                    locationPlayer.messagePlayerLocation(recipient);
                });
    }

}
