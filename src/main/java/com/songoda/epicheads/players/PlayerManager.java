package com.songoda.epicheads.players;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private static final Map<UUID, EPlayer> registeredHeads = new HashMap<>();

    public EPlayer getPlayer(UUID uuid) {
        return registeredHeads.computeIfAbsent(uuid, u -> new EPlayer(uuid));
    }

    public EPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

}
