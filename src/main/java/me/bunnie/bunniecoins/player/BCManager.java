package me.bunnie.bunniecoins.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BCManager {

    private Map<UUID, BCPlayer> playerMap;

    public BCManager() {
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
        playerMap.put(bcPlayer.getUuid(), bcPlayer);
    }
}
