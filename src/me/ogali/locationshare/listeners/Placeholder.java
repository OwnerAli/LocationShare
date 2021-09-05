package me.ogali.locationshare.listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Placeholder implements Listener {

	private final Map<String, Long> placeholderCooldown = new HashMap<>();

	private final Plugin pl;

	public Placeholder(Plugin pl) {
		this.pl = pl;
	}

	DecimalFormat format = new DecimalFormat("###.###");

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (!(e.getMessage().contains("[Location]"))) return;

		String name = e.getPlayer().getName();
		Player p = e.getPlayer();
		boolean cancelMessage = pl.getConfig().getBoolean("Placeholder.Cancel-Message");
		boolean sendPermMessage = pl.getConfig().getBoolean("Placeholder.Permission-Message");

		// PERMISSION CHECK
		if (!(p.hasPermission(pl.getConfig().getString("Permission.Placeholder")))) {
			if (cancelMessage) {
				e.setCancelled(true);
			}
			if (sendPermMessage) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Permission.No-Perm")));
			}
			return;
		}

		// COOLDOWN
		if (placeholderCooldown.containsKey(name)) {
			if (placeholderCooldown.get(name) > System.currentTimeMillis()) {
				long timeLeft = (placeholderCooldown.get(name) - System.currentTimeMillis()) / 1000;
				String time = String.valueOf(timeLeft);
				String message = pl.getConfig().getString("Cooldown.Message").replace("@time", time);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				e.setCancelled(true);
				return;
			}
		}
		if (!(p.hasPermission(pl.getConfig().getString("Permission.No-Cooldown")))) {
			placeholderCooldown.put(name, System.currentTimeMillis() + (pl.getConfig().getLong("Cooldown.Placeholder-Time") * 1000));
		}

		// [Location] (TRIGGER MESSAGE)
		if (e.getMessage().contains("[Location]")) {
			String loc = pl.getConfig().getString("General.Placeholder-Message").
					replace("@player", name).replace("@location",
					(format.format(p.getLocation().getX())) + ", " + format.format(p.getLocation().getZ()));
			String message = e.getMessage().replace("[Location]", ChatColor.translateAlternateColorCodes('&', loc));
			e.setMessage(message);
		}
	}
}
