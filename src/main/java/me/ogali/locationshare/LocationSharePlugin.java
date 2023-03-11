package me.ogali.locationshare;

import co.aikar.commands.PaperCommandManager;
import me.ogali.locationshare.commands.AdminCommands;
import me.ogali.locationshare.commands.ShareCommands;
import me.ogali.locationshare.listeners.ChatPlaceholderListener;
import me.ogali.locationshare.listeners.PlayerJoinListener;
import me.ogali.locationshare.registries.LocationPlayerRegistry;
import me.ogali.locationshare.util.Chat;
import org.bukkit.plugin.java.JavaPlugin;

public class LocationSharePlugin extends JavaPlugin {

    public static LocationSharePlugin instance;
    private LocationPlayerRegistry locationPlayerRegistry;

    @Override
    public void onEnable() {
        initializePlugin();
//        this.getCommand("LS").setExecutor(new ls(this));
    }

    @Override
    public void onDisable() {
        Chat.log("&f[&d&lLS&f] Location Share &c&lDisabled!");
    }

    public LocationPlayerRegistry getLocationPlayerRegistry() {
        return locationPlayerRegistry;
    }

    public static LocationSharePlugin getInstance() {
        return instance;
    }

    private void initializePlugin() {
        instance = this;
        initializeConfig();
        registerCommands();
        registerListeners();
        initializeRegistries();
        Chat.log("&f[&d&lLS&f] Location Share &a&lEnabled!");
    }

    private void initializeConfig() {
        saveDefaultConfig();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new ChatPlaceholderListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    private void registerCommands() {
        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new AdminCommands(this));
        paperCommandManager.registerCommand(new ShareCommands(this));
    }

    private void initializeRegistries() {
        locationPlayerRegistry = new LocationPlayerRegistry();
    }

}
