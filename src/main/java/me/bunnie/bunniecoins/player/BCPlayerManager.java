package me.bunnie.bunniecoins.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BCPlayerManager {

    private final Map<UUID, BCPlayer> playerMap;

    public BCPlayerManager() {
        playerMap = new HashMap<>();
    }

    public BCPlayer findBCPlayerByUUID(UUID uuid) {
        return playerMap
                .values()
                .stream()
                .filter(bcPlayer -> bcPlayer.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public void addBCPlayerToCache(BCPlayer bcPlayer) {
        if(playerMap.containsKey(bcPlayer.getUuid())) return;
        playerMap.put(bcPlayer.getUuid(), bcPlayer);
    }

    public void removeBCPlayerFromCache(BCPlayer bcPlayer) {
        if(!playerMap.containsKey(bcPlayer.getUuid())) return;
        playerMap.remove(bcPlayer.getUuid(), bcPlayer);
    }

    public List<BCPlayer> findAllBCPlayers() {
        return playerMap.values()
                .stream().toList();
    }

}
