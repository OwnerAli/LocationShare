package me.ogali.locationshare.registries;

import me.ogali.locationshare.players.LocationPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LocationPlayerRegistry {

    private final Set<LocationPlayer> locationPlayerSet = new HashSet<>();

    public void registerLocationPlayer(Player player) {
        locationPlayerSet.add(new LocationPlayer(player));
    }

    public Optional<LocationPlayer> getLocationPlayer(Player player) {
        return locationPlayerSet
                .stream()
                .filter(locationPlayer -> locationPlayer.getPlayer().equals(player))
                .findFirst();
    }

}
