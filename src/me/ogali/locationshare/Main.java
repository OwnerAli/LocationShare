package me.ogali.locationshare;

import me.ogali.locationshare.commands.ls;
import me.ogali.locationshare.listeners.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("LS").setExecutor(new ls(this));
        this.getServer().getPluginManager().registerEvents(new Placeholder(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&d&lLS&f] Location Share &a&lEnabled!"));
        config();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&d&lLS&f] Location Share &c&lDisabled!"));
    }

    public void config() {
        saveDefaultConfig();
    }
}
