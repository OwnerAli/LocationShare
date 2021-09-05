package me.ogali.locationshare.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ls implements CommandExecutor {

    private final Map<String, Long> commandCooldown = new HashMap<>();

    private final Plugin pl;

    public ls(Plugin pl) {
        this.pl = pl;
    }

    DecimalFormat format = new DecimalFormat("###.###");

    @SuppressWarnings("deprecation")
    private TextComponent sendJsonLocation(String loc, String name, Player player) {
        TextComponent message = null;
        if (pl.getConfig().getString("General.Tp-Message").contains("@name")) {
            message = new TextComponent(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Tp-Message").replace("@name", player.getName()).replace("@location", loc)));
        } else if ((pl.getConfig().getString("General.Tp-Message").contains("@display"))) {
            message = new TextComponent(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Tp-Message").replace("@display", player.getDisplayName()).replace("@location", loc)));
        }
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, pl.getConfig().getString("General.Tp-Command").replace("@player", name)));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Tp-Hover-Message"))).create()));
        return message;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender s, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage("NAUGHTY, NAUGHTY!");
            return false;
        }
        Player player = (Player) s;

        // RELOAD && ARG CHECK
        if (label.equalsIgnoreCase("LS")) {
            if (!(player.hasPermission(pl.getConfig().getString("Permission.Ls-Command"))
                    || !(player.hasPermission(pl.getConfig().getString("Permission.Ls-Tp-Command"))))) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Permission.No-Perm")));
                return false;
            }
            if (args.length == 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Usage-Message")));
                return false;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission(pl.getConfig().getString("Permission.Reload"))) {
                    pl.reloadConfig();
                    pl.saveConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig Reloaded!"));
                    return true;
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Permission.No-Perm")));
                    return false;
                }
            }

            // COOLDOWN
            if (commandCooldown.containsKey(player.getName())) {
                if (commandCooldown.get(player.getName()) > System.currentTimeMillis()) {
                    long timeLeft = (commandCooldown.get(player.getName()) - System.currentTimeMillis()) / 1000;
                    String time = String.valueOf(timeLeft);
                    String message = pl.getConfig().getString("Cooldown.Message").replace("@time", time);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    return false;
                }
            }
            if (!(player.hasPermission(pl.getConfig().getString("Permission.No-Cooldown")))) {
                commandCooldown.put(player.getName(), System.currentTimeMillis() + (pl.getConfig().getLong("Cooldown.Command-Time") * 1000));
            }

            // LS TP COMMAND
            String loc = format.format(player.getLocation().getX()) + ", " + format.format(player.getLocation().getZ());
            String name = player.getName();

            if (args[0].equalsIgnoreCase("tp")) {
                if (!(player.hasPermission(pl.getConfig().getString("Permission.Ls-Tp-Command")))) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Permission.No-Perm")));
                    return false;
                }
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("all")) {
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            players.spigot().sendMessage(sendJsonLocation(loc, name, player));
                        }
                        return false;
                    } else if (Bukkit.getOfflinePlayer(args[1]).getPlayer() != null) {
                        Bukkit.getOfflinePlayer(args[1]).getPlayer().spigot().sendMessage(sendJsonLocation(loc, name, player));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Success")));
                        return false;
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Invalid-Player")));
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Tp-Usage-Message")));
                    return false;
                }
                return true;
            }

            // LS COMMAND
            String message = loc;
            Player p = Bukkit.getOfflinePlayer(args[0]).getPlayer();
            if (!(player.hasPermission(pl.getConfig().getString("Permission.Ls-Command")))) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Permission.No-Perm")));
                return false;
            }
            if (p == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Invalid-Player")));
                return true;
            }
            if (pl.getConfig().getString("General.Location-Message").contains("@name")) {
                message = pl.getConfig().getString("General.Location-Message").replace("@name", player.getName()).replace("@location", loc);
            } else if (pl.getConfig().getString("General.Location-Message").contains("@display")) {
                message = pl.getConfig().getString("General.Location-Message").replace("@display", player.getDisplayName()).replace("@location", loc);
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("General.Success")));
        }
        return false;
    }
}