package me.ogali.locationshare.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.ogali.locationshare.LocationSharePlugin;
import me.ogali.locationshare.util.Chat;
import org.bukkit.entity.Player;

@CommandAlias("locationshare|ls")
public class AdminCommands extends BaseCommand {

    private final LocationSharePlugin main;

    public AdminCommands(LocationSharePlugin main) {
        this.main = main;
    }

    @Subcommand("reload")
    @CommandPermission("ls.reload")
    public void onReload(Player player) {
        main.reloadConfig();
        main.saveConfig();
        Chat.tell(player, "&aConfig Reloaded!");
    }

}