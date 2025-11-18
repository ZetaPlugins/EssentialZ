package com.zetaplugins.essentialz.features;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LastMsgManager {
    Map<UUID, UUID> lastMsgMap;

    public LastMsgManager() {
        lastMsgMap = new HashMap<>();
    }

    public void setLastMsg(UUID sender, UUID receiver) {
        lastMsgMap.put(sender, receiver);
    }

    public UUID getLastMsg(UUID sender) {
        return lastMsgMap.get(sender);
    }

}
